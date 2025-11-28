package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.ZipFileService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DebtPositionRetrieverServiceImpl implements DebtPositionRetrieverService {

  private final DebtPositionService debtPositionService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionViewMapper debtPositionViewMapper;
  private final DebtPositionMapper debtPositionMapper;
  private final PrintPaymentNoticeService printPaymentNoticeService;
  private final ZipFileService zipFileService;

  private static final List<String> debtPositionOriginFilterList = List.of(
    DebtPositionOrigin.ORDINARY.toString(),
    DebtPositionOrigin.ORDINARY_SIL.toString(),
    DebtPositionOrigin.SPONTANEOUS.toString(),
    DebtPositionOrigin.SPONTANEOUS_SIL.toString()
  );

  public DebtPositionRetrieverServiceImpl(DebtPositionService debtPositionService,
                                          DebtPositionTypeOrgService debtPositionTypeOrgService,
                                          DebtPositionViewMapper debtPositionViewMapper,
                                          DebtPositionMapper debtPositionMapper,
                                          PrintPaymentNoticeService printPaymentNoticeService,
                                          ZipFileService zipFileService) {
    this.debtPositionService = debtPositionService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionViewMapper = debtPositionViewMapper;
    this.debtPositionMapper = debtPositionMapper;
    this.printPaymentNoticeService = printPaymentNoticeService;
    this.zipFileService = zipFileService;
  }

  @Override
  public DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser);
    if (debtPositionDTO.getDebtPositionId() != null) {
      throw new InvalidDebtPositionException("Bad Request: Debt Position ID should not be provided");
    }
    debtPositionDTO.setDebtPositionOrigin(DebtPositionOrigin.ORDINARY);
    debtPositionDTO.getPaymentOptions().forEach(paymentOptionDTO ->
      paymentOptionDTO.getInstallments().forEach(installmentDTO -> installmentDTO.setGenerateNotice(true))
    );
    return debtPositionService.createDebtPosition(debtPositionDTO, false, accessToken);
  }

  @Override
  public PagedDebtPositionView getDebtPositionViews(
    DebtPositionViewFiltersDTO filtersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {

    AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser);

    validateDebtPositionViewFilters(filtersDTO);

    return debtPositionViewMapper.mapToPagedDebtPositionView(
      debtPositionService.getDebtPositionViews(
        filtersDTO,
        debtPositionOriginFilterList,
        loggedUser.getMappedExternalUserId(),
        pageable,
        accessToken));
  }

  private void validateDebtPositionViewFilters(DebtPositionViewFiltersDTO filtersDTO) {
    if (DateUtils.isNullOrInvalidOffsetDateTimeRange(filtersDTO.getCreationDateTimeFrom(), filtersDTO.getCreationDateTimeTo()) &&
      StringUtils.isBlank(filtersDTO.getFiscalCode()) &&
      filtersDTO.getDebtPositionTypeOrgId() == null &&
      filtersDTO.getStatus() == null &&
      filtersDTO.getIuv() == null &&
      filtersDTO.getIud() == null) {
      throw new IllegalArgumentException("At least one of the research fields must be provided, and both 'from' and 'to' creation dates must be set together");
    }
  }

  @Override
  public DebtPositionDetailDTO getDebtPositionDetail(Long debtPositionId,
                                                     Long organizationId,
                                                     UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    DebtPositionDTO debtPosition = debtPositionService.getDebtPosition(debtPositionId, accessToken);
    if (debtPosition != null) {
      // TODO chiamare it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService.findByDebtPositionTypeOrgIdAndOperatorExternalUserId se è null torno null, se diverso da null prosegui con il map
      return debtPositionMapper.mapToDebtPositionDetailDTO(
        debtPosition,
        debtPositionTypeOrgService.getDebtPositionTypeOrg(debtPosition.getDebtPositionTypeOrgId(), accessToken)
      );
    } else {
      return null;
    }
  }

  @Override
  public boolean deleteDebtPosition(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return debtPositionService.deleteDebtPosition(debtPositionId, accessToken);
  }

  @Override
  public Resource getDebtPositionNoticesZip(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

    DebtPositionDTO debtPosition = debtPositionService.getDebtPosition(debtPositionId, accessToken);
    if (debtPosition == null) {
      return null;
    }

    List<FileResourceDTO> pdfResources = debtPosition
      .getPaymentOptions()
      .stream()
      .flatMap(po ->
        po.getInstallments()
          .stream()
          .filter(
            i ->
              (InstallmentStatus.UNPAID.equals(i.getStatus()) ||
                InstallmentStatus.UNPAYABLE.equals(i.getStatus())))
      )
      .map(i ->
        printPaymentNoticeService.generateNotice(i.getIuv(), debtPosition, accessToken))
      .toList();

    if (pdfResources.isEmpty()) {
      return null;
    }

    return zipFileService.zipper(pdfResources);
  }

  public void validateOperator(Long debtPositionId, Long organizationId, UserInfo loggedUser, String accessToken) {
    boolean hasOperatorGrantOnDebtPosition = debtPositionService.hasOperatorGrantOnDebtPosition(
      debtPositionId,
      organizationId,
      loggedUser.getMappedExternalUserId(),
      accessToken
    );

    if (!hasOperatorGrantOnDebtPosition) {
      throw new ResourceNotFoundException("DebtPosition with debtPositionId "+debtPositionId+" and organizationId "+organizationId+" not found");
    }
  }

  @Override
  public DebtPositionDTO manageDebtPositionInstallments(Long organizationId, Long debtPositionId, ManageDebtPositionDTO manageDebtPositionDTO, Boolean publish, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

    DebtPositionDTO updatedDebtPosition = debtPositionService.manageDebtPositionInstallments(debtPositionId, manageDebtPositionDTO, accessToken);
    if (checkDebtPositionPublishCondition(publish, updatedDebtPosition)) {
      try {
        return debtPositionService.publishDebtPosition(debtPositionId, accessToken);
      } catch (ResourceNotFoundException | ConflictException e) {
        log.warn(e.getMessage());
      }
    }
    return updatedDebtPosition;
  }

  private static boolean checkDebtPositionPublishCondition(Boolean publish, DebtPositionDTO updatedDebtPosition) {
    return Boolean.TRUE.equals(publish)
      && updatedDebtPosition != null
      && DebtPositionStatus.DRAFT.equals(updatedDebtPosition.getStatus());
  }

  @Override
  public DebtPositionDTO publishDebtPosition(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

    return debtPositionService.publishDebtPosition(debtPositionId, accessToken);
  }
}
