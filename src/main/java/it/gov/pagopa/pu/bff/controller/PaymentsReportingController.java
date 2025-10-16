package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PaymentsReportingApi;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.payments_reporting.PaymentsReportingRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@Slf4j
@RestController
public class PaymentsReportingController implements PaymentsReportingApi {

  private final PaymentsReportingRetrieverService paymentsReportingRetrieverService;

  public PaymentsReportingController(PaymentsReportingRetrieverService paymentsReportingRetrieverService) {
    this.paymentsReportingRetrieverService = paymentsReportingRetrieverService;
  }

  @Override
  public ResponseEntity<PagedPaymentsReportingView> getPaymentsReporting(Long organizationId, String iuf, String iuv, String regulationUniqueIdentifier, OffsetDateTime regulationDateTimeFrom, OffsetDateTime regulationDateTimeTo, Pageable pageable) {
    log.info("User requested getPaymentsReporting having organizationId {}", organizationId);
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(regulationDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(regulationDateTimeTo));

    return ResponseEntity.ok(paymentsReportingRetrieverService.getPaymentsReporting(
      organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, iuv, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedPaymentsReportingRow> getPaymentsReportingRows(
    Long organizationId, String iuf, String iuv, OffsetDateTime payDateTimeFrom,
    OffsetDateTime payDateTimeTo,
    Pageable pageable) {
    log.info("User requested getPaymentsReportingRows having organizationId {} and iuf {}", organizationId, iuf);
    LocalDateIntervalFilter payDateIntervalFilter = new LocalDateIntervalFilter(DateUtils.fromOffsetDateTimeToLocalDate(payDateTimeFrom), DateUtils.fromOffsetDateTimeToLocalDate(payDateTimeTo));
    return ResponseEntity.ok(
      paymentsReportingRetrieverService.getPaymentsReportingRows(
        organizationId, iuf, iuv, payDateIntervalFilter,
        pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PaymentsReportingDetailDTO> getPaymentsReportingDetail(Long organizationId, String iuf, String paymentsReportingId) {
    log.info("User requested getPaymentsReportingDetail having organizationId {} and paymentsReportingId {}", organizationId, paymentsReportingId);
    return ResponseEntity.ofNullable(paymentsReportingRetrieverService.getPaymentsReportingDetail(
      organizationId, iuf, paymentsReportingId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
