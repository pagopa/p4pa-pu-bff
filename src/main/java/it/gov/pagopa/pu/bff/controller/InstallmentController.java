package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.controller.generated.InstallmentsApi;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@Slf4j
@RestController
public class InstallmentController implements InstallmentsApi {

  private final InstallmentRetrieverService installmentRetrieverService;

  public InstallmentController(InstallmentRetrieverService installmentRetrieverService) {
    this.installmentRetrieverService = installmentRetrieverService;
  }

  @Override
  public ResponseEntity<PagedInstallmentView> getInstallments(Long organizationId, OffsetDateTime dueDateTimeFrom, OffsetDateTime dueDateTimeTo, String iuv, String fiscalCode, Long debtPositionTypeOrgId, Pageable pageable) {
    log.info("User requested getInstallments having organizationId {}, dueDateTimeFrom {} and dueDateTimeTo {}", organizationId, dueDateTimeFrom, dueDateTimeTo);
    UserInfo userInfo = SecurityUtils.getLoggedUser();
    LocalDateIntervalFilter dueDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(dueDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(dueDateTimeTo));

    return ResponseEntity.ok(installmentRetrieverService.getInstallments(
      new InstallmentViewFiltersDTO(
        organizationId, userInfo.getMappedExternalUserId(), dueDateFilter, iuv, fiscalCode, Collections.emptyList(), debtPositionTypeOrgId), pageable, userInfo, SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<InstallmentDetailDTO> getInstallmentDetail(Long organizationId, Long installmentId) {
    log.info("User requested getInstallmentDetail having organizationId {} and installmentId {}", organizationId, installmentId);
    return ResponseEntity.ofNullable(installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
