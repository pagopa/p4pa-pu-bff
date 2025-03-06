package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingViewSearchClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingServiceImpl implements PaymentsReportingService {

  private final PaymentsReportingViewSearchClient paymentsReportingViewSearchClient;
  private final PaymentsReportingSearchClient paymentsReportingSearchClient;

  public PaymentsReportingServiceImpl(PaymentsReportingViewSearchClient paymentsReportingViewSearchClient,
    PaymentsReportingSearchClient paymentsReportingSearchClient) {
    this.paymentsReportingViewSearchClient = paymentsReportingViewSearchClient;
    this.paymentsReportingSearchClient = paymentsReportingSearchClient;
  }

  @Override
  public PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter,
                                                              Pageable pageable, String accessToken) {
    return paymentsReportingViewSearchClient.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken);
  }

  @Override
  public PagedModelPaymentsReporting getPaymentsReportingDetail(Long organizationId, String iuf, String iuv, LocalDateIntervalFilter payDateFilter,
                                                              Pageable pageable, String accessToken) {
    return paymentsReportingSearchClient.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, accessToken);
  }
}
