package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingSearchClient {

  private final ClassificationApisHolder classificationApisHolder;

  public PaymentsReportingSearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelPaymentsReporting getPaymentsReportingRows(Long organizationId, String iuf, String iuv, LocalDateIntervalFilter payDateFilter,
    Pageable pageable, String accessToken) {
    return classificationApisHolder.getPaymentsReportingSearchControllerApi(accessToken)
      .crudPaymentsReportingFindPaymentsReportingByFilters(
        organizationId,
        iuf,
        iuv,
        payDateFilter.getFrom(),
        payDateFilter.getTo(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public PaymentsReporting getPaymentsReportingDetail(Long organizationId, String paymentsReportingId, String accessToken) {
    return classificationApisHolder.getPaymentsReportingSearchControllerApi(accessToken)
      .crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(organizationId, paymentsReportingId);
  }
}
