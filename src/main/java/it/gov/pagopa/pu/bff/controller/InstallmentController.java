package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.controller.generated.InstallmentsApi;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
public class InstallmentController implements InstallmentsApi {

  private final InstallmentRetrieverService installmentRetrieverService;

  public InstallmentController(InstallmentRetrieverService installmentRetrieverService) {
    this.installmentRetrieverService = installmentRetrieverService;
  }

  @Override
  public ResponseEntity<PagedInstallmentView> getInstallments(Long organizationId, OffsetDateTime dueDateFrom, OffsetDateTime dueDateTo, String iuv, String fiscalCode, Long debtPositionTypeOrgId, Pageable pageable) {
    UserInfo userInfo = SecurityUtils.getLoggedUser();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(dueDateFrom, dueDateTo);

    return ResponseEntity.ok(installmentRetrieverService.getInstallments(
      new InstallmentViewFiltersDTO(
        organizationId, userInfo.getMappedExternalUserId(), paymentDateTimeFilter, iuv, fiscalCode, debtPositionTypeOrgId), pageable, userInfo, SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<InstallmentDetailDTO> getInstallmentDetail(Long organizationId, Long installmentId) {
    return ResponseEntity.ofNullable(installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
