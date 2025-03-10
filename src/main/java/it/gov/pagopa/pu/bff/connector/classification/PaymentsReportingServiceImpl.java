package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingServiceImpl implements PaymentsReportingService {

  private final PaymentsReportingClient client;

  public PaymentsReportingServiceImpl(PaymentsReportingClient client) {
    this.client = client;
  }

  @Override
  public PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter,
                                                              Pageable pageable, String accessToken) {
    return client.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken);
  }

  @Override
  public PaymentsReporting getPaymentsReportingDetail(Long organizationId,
    String paymentsReportingId, String accessToken) {
    return client.getPaymentsReportingDetail(organizationId, paymentsReportingId, accessToken);
  }
}
