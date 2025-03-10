package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingClient {

  private final ClassificationApisHolder classificationApisHolder;

  public PaymentsReportingClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter,
                                                              Pageable pageable, String accessToken) {
    return classificationApisHolder.getPaymentsReportingViewSearchControllerApi(accessToken)
      .crudPaymentsReportingViewFindDistinctByIufAndRegulationUniqueIdentifier(
        String.valueOf(organizationId),
        iuf,
        regulationUniqueIdentifier,
        regulationDateFilter.getFrom(),
        regulationDateFilter.getTo(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public PaymentsReporting getPaymentsReportingDetail(Long organizationId, String paymentsReportingId, String accessToken) {
    return classificationApisHolder.getPaymentsReportingSearchControllerApi(accessToken)
      .crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(organizationId, paymentsReportingId);
  }

}
