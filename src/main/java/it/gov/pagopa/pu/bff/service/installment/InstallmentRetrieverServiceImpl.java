package it.gov.pagopa.pu.bff.service.installment;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstallmentRetrieverServiceImpl implements InstallmentRetrieverService {

  private final InstallmentViewMapper installmentViewMapper;
  private final InstallmentService installmentService;
  private final List<InstallmentStatus> statusList = List.of(InstallmentStatus.PAID, InstallmentStatus.REPORTED);

  public InstallmentRetrieverServiceImpl(
    InstallmentViewMapper installmentViewMapper,
    InstallmentService installmentService) {
    this.installmentViewMapper = installmentViewMapper;
    this.installmentService = installmentService;
  }

  @Override
  public PagedInstallmentView getInstallments(InstallmentViewFiltersDTO installmentViewFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(installmentViewFiltersDTO.getOrganizationId(), loggedUser);

    validateInstallmentViewFilters(installmentViewFiltersDTO);

    return installmentViewMapper.mapToPagedInstallmentView(
      installmentService.getInstallments(installmentViewFiltersDTO, pageable, accessToken));
  }

  private void validateInstallmentViewFilters(InstallmentViewFiltersDTO filtersDTO) {
    if ((filtersDTO.getDueDate() == null ||
      DateUtils.isNullOrInvalidLocalDateRange(filtersDTO.getDueDate().getFrom(), filtersDTO.getDueDate().getTo())) &&
      StringUtils.isBlank(filtersDTO.getIuv()) &&
      StringUtils.isBlank(filtersDTO.getFiscalCode()) &&
      filtersDTO.getDebtPositionTypeOrgId() == null) {
      throw new IllegalArgumentException("At least one of the research fields must be provided, and both 'from' and 'to' due dates must be set together");
    }
  }

  @Override
  public InstallmentDetailDTO getInstallmentDetail(Long organizationId, Long installmentId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    InstallmentDetailDTO installmentDetailDTO = installmentService.getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
    setPaymentInfo(installmentDetailDTO);
    return installmentDetailDTO;
  }

  @Override
  public InstallmentNoPII getInstallmentFromTransferSemanticKey(
    Long organizationId, String iuv, String iur, String transferIndex, UserInfo loggedUser, List<DebtPositionOrigin> debtPositionOrigins, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    return installmentService.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex,
      loggedUser.getMappedExternalUserId(), debtPositionOrigins, accessToken);
  }


  private void setPaymentInfo(InstallmentDetailDTO installmentDetailDTO) {
    if (!statusList.contains(installmentDetailDTO.getStatus())) {
      installmentDetailDTO.setPayer(null);
      installmentDetailDTO.setPaymentDateTime(null);
      installmentDetailDTO.setIud(null);
      installmentDetailDTO.setIur(null);
      installmentDetailDTO.setPspCompanyName(null);
    }
  }

}
