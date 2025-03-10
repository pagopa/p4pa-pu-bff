package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingViewSearchClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingServiceImpl implements PaymentsReportingService {

  private final PaymentsReportingViewSearchClient paymentsReportingViewSearchClient;
  private final PaymentsReportingSearchClient paymentsReportingSearchClient;

  public PaymentsReportingServiceImpl(
    PaymentsReportingViewSearchClient paymentsReportingViewSearchClient,
    PaymentsReportingSearchClient paymentsReportingSearchClient) {
    this.paymentsReportingViewSearchClient = paymentsReportingViewSearchClient;
    this.paymentsReportingSearchClient = paymentsReportingSearchClient;
  }

  @Override
  public PagedModelPaymentsReportingView getPaymentsReporting(
    Long organizationId, String iuf, String regulationUniqueIdentifier,
    LocalDateIntervalFilter regulationDateFilter,
    Pageable pageable, String accessToken) {
    return paymentsReportingViewSearchClient.getPaymentsReporting(
      organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter,
      pageable, accessToken);
  }

  @Override
  public PagedModelPaymentsReporting getPaymentsReportingRows(
    Long organizationId, String iuf, String iuv,
    LocalDateIntervalFilter payDateFilter,
    Pageable pageable, String accessToken) {
    return paymentsReportingSearchClient.getPaymentsReportingRows(
      organizationId, iuf, iuv, payDateFilter, pageable, accessToken);
  }

  @Override
  public PaymentsReporting getPaymentsReportingDetail(Long organizationId,
    String paymentsReportingId, String accessToken) {
    return paymentsReportingSearchClient.getPaymentsReportingDetail(
      organizationId, paymentsReportingId, accessToken);
  }
}
