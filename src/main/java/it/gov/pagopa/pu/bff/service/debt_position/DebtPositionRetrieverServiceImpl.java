package it.gov.pagopa.pu.bff.service.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.ZipFileService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
public class DebtPositionRetrieverServiceImpl implements DebtPositionRetrieverService {

  private final DebtPositionService debtPositionService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionViewMapper debtPositionViewMapper;
  private final DebtPositionMapper debtPositionMapper;
  private final DebtPositionNoticeRetrieverService debtPositionNoticeRetrieverService;
  private final ZipFileService zipFileService;
  private final Path workingDirectory;
  private static final List<String> debtPositionOriginFilterList = List.of(
    DebtPositionOrigin.ORDINARY.toString(),
    DebtPositionOrigin.ORDINARY_SIL.toString(),
    DebtPositionOrigin.SPONTANEOUS.toString()
  );

  public DebtPositionRetrieverServiceImpl(DebtPositionService debtPositionService,
                                          DebtPositionTypeOrgService debtPositionTypeOrgService,
                                          DebtPositionViewMapper debtPositionViewMapper,
                                          DebtPositionMapper debtPositionMapper,
                                          DebtPositionNoticeRetrieverService debtPositionNoticeRetrieverService,
                                          ZipFileService zipFileService,
                                          @Value("${folders.tmp}")Path workingDirectory) {
    this.debtPositionService = debtPositionService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionViewMapper = debtPositionViewMapper;
    this.debtPositionMapper = debtPositionMapper;
    this.debtPositionNoticeRetrieverService = debtPositionNoticeRetrieverService;
    this.zipFileService = zipFileService;
    this.workingDirectory = workingDirectory;
  }

  @Override
  public DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser);
    if (debtPositionDTO.getDebtPositionId() != null) {
      throw new InvalidDebtPositionException("Bad Request: Debt Position ID should not be provided");
    }
    return debtPositionService.createDebtPosition(debtPositionDTO, false, accessToken);
  }

  @Override
  public PagedDebtPositionView getDebtPositionViews(
    DebtPositionViewFiltersDTO filtersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser);
    return debtPositionViewMapper.mapToPagedDebtPositionView(
      debtPositionService.getDebtPositionViews(
        filtersDTO,
        debtPositionOriginFilterList,
        loggedUser.getMappedExternalUserId(),
        pageable,
        accessToken)
    );
  }

  @Override
  public DebtPositionDetailDTO getDebtPositionDetail(Long debtPositionId,
                                                     Long organizationId,
                                                     UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    DebtPositionDTO debtPosition = debtPositionService.getDebtPosition(debtPositionId, accessToken);
    if (debtPosition != null) {
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
    return debtPositionService.deleteDebtPosition(debtPositionId,accessToken);
  }

  @Override
  public Resource getDebtPositionNoticesZip(Long organizationId, Long debtPositionId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    DebtPositionDTO debtPosition = debtPositionService.getDebtPosition(debtPositionId, accessToken);
    if (debtPosition == null){
      return null;
    }

    List<FileResourceDTO> pdfResources = debtPosition
      .getPaymentOptions()
      .stream()
      .flatMap(po ->
        po.getInstallments()
          .stream()
          .filter(
            i -> i.getStatus() != null &&
              (InstallmentStatus.UNPAID.equals(i.getStatus()) ||
                InstallmentStatus.UNPAYABLE.equals(i.getStatus())))
      )
      .map(i ->
        debtPositionNoticeRetrieverService.getNotice(organizationId, i.getIuv(), debtPositionId, loggedUser, accessToken))
      .toList();

    if (pdfResources.isEmpty()){
      return null;
    }

    return zipFileService.createZipFromResources(pdfResources, workingDirectory, buildZipFileName(organizationId, debtPositionId));

  }

  private String buildZipFileName(Long organizationId, Long debtPositionId) {
    return organizationId + "_" + debtPositionId + "_PDF.zip";
  }

}
