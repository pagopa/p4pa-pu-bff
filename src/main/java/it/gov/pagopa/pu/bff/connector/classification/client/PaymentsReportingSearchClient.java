package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class PaymentsReportingSearchClient {

  private final ClassificationApisHolder classificationApisHolder;

  public PaymentsReportingSearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PaymentsReporting getPaymentsReportingDetail(Long organizationId, String paymentsReportingId, String accessToken) {
    try {
      return classificationApisHolder.getPaymentsReportingSearchControllerApi(
          accessToken)
        .crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(
          organizationId, paymentsReportingId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("PaymentsReporting with paymentsReportingId {} not found", paymentsReportingId);
      return null;
    }
  }
}
