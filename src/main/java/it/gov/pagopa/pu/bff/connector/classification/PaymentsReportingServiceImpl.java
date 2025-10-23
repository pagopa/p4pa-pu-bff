package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingViewSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingWithReceiptViewSearchClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingServiceImpl implements PaymentsReportingService {

  private final PaymentsReportingViewSearchClient paymentsReportingViewSearchClient;
  private final PaymentsReportingSearchClient paymentsReportingSearchClient;
  private final PaymentsReportingWithReceiptViewSearchClient paymentsReportingWithReceiptViewSearchClient;

  public PaymentsReportingServiceImpl(
    PaymentsReportingViewSearchClient paymentsReportingViewSearchClient,
    PaymentsReportingSearchClient paymentsReportingSearchClient,
    PaymentsReportingWithReceiptViewSearchClient paymentsReportingWithReceiptViewSearchClient) {
    this.paymentsReportingViewSearchClient = paymentsReportingViewSearchClient;
    this.paymentsReportingSearchClient = paymentsReportingSearchClient;
    this.paymentsReportingWithReceiptViewSearchClient = paymentsReportingWithReceiptViewSearchClient;
  }

  @Override
  public PagedModelPaymentsReportingView getPaymentsReporting(
    Long organizationId, String iuf, String regulationUniqueIdentifier,
    LocalDateIntervalFilter regulationDateFilter, String iuv,
    Pageable pageable, String accessToken) {
    return paymentsReportingViewSearchClient.getPaymentsReporting(
      organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, iuv,
      pageable, accessToken);
  }

  @Override
  public PagedModelPaymentsReportingWithReceiptView getPaymentsReportingRows(
    Long organizationId, String iuf, String iuv,
    LocalDateIntervalFilter payDateFilter,
    Pageable pageable, String accessToken) {
    return paymentsReportingWithReceiptViewSearchClient.getPaymentsReportingRows(
      organizationId, iuf, iuv, payDateFilter, pageable, accessToken);
  }

  @Override
  public PaymentsReporting getPaymentsReportingDetail(Long organizationId,
    String paymentsReportingId, String accessToken) {
    return paymentsReportingSearchClient.getPaymentsReportingDetail(
      organizationId, paymentsReportingId, accessToken);
  }
}
