package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingWithReceiptViewSearchClient {

  private final ClassificationApisHolder classificationApisHolder;

  public PaymentsReportingWithReceiptViewSearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelPaymentsReportingWithReceiptView getPaymentsReportingRows(Long organizationId, String iuf, String iuv, LocalDateIntervalFilter payDateFilter,
                                                                        Pageable pageable, String accessToken) {
    return classificationApisHolder.getPaymentsReportingWithReceiptViewSearchControllerApi(accessToken)
      .crudPaymentsReportingWithReceiptViewFindPaymentsReportingByFilters(
        organizationId,
        iuf,
        iuv,
        payDateFilter.getFrom(),
        payDateFilter.getTo(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
