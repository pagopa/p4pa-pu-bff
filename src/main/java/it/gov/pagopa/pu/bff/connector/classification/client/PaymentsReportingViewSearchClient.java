package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsReportingViewSearchClient {

  private final ClassificationApisHolder classificationApisHolder;

  public PaymentsReportingViewSearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedModelPaymentsReportingView getPaymentsReporting(Long organizationId, String iuf, String regulationUniqueIdentifier, LocalDateIntervalFilter regulationDateFilter,
                                                              Pageable pageable, String accessToken) {
    return classificationApisHolder.getPaymentsReportingViewSearchControllerApi(accessToken)
      .crudPaymentsReportingViewFindDistinctByIufAndRegulationUniqueIdentifierAndIuv(
        String.valueOf(organizationId),
        iuf,
        regulationUniqueIdentifier,
        regulationDateFilter.getFrom(),
        regulationDateFilter.getTo(),
        null,//TODO P4ADEV-3852 add iuv filter
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
