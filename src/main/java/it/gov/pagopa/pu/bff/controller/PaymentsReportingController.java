package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PaymentsReportingApi;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReporting;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.payments_reporting.PaymentsReportingRetrieverService;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentsReportingController implements PaymentsReportingApi {

  private final PaymentsReportingRetrieverService paymentsReportingRetrieverService;

  public PaymentsReportingController(PaymentsReportingRetrieverService paymentsReportingRetrieverService) {
    this.paymentsReportingRetrieverService = paymentsReportingRetrieverService;
  }

  @Override
  public ResponseEntity<PagedPaymentsReportingView> getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDate regulationDateFrom, LocalDate regulationDateTo, Pageable pageable) {
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);

    return ResponseEntity.ok(paymentsReportingRetrieverService.getPaymentsReporting(
      organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedPaymentsReporting> getPaymentsReportingDetail(
    Long organizationId, String iuf, String iuv, LocalDate payDateFrom,
    LocalDate payDateTo,
    Pageable pageable) {
    return ResponseEntity.ok(
      paymentsReportingRetrieverService.getPaymentsReportingDetail(
        organizationId, iuf, iuv, new LocalDateIntervalFilter(payDateFrom,payDateTo),
        pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
