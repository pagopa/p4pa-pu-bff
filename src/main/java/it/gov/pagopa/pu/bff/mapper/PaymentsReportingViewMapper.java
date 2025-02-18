package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class PaymentsReportingViewMapper {

  public PagedPaymentsReportingView mapToPagedPaymentsReporting(PagedModelPaymentsReportingView pagedModel) {
    PagedPaymentsReportingView mappedPaymentsReporting = new PagedPaymentsReportingView();

    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(pagedModel.getEmbedded().getPaymentsReportingViews())) {
        mappedPaymentsReporting.setContent(pagedModel.getEmbedded().getPaymentsReportingViews());
      } else {
        mappedPaymentsReporting.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        mappedPaymentsReporting.setTotalPages(pagedModel.getPage().getTotalPages());
        mappedPaymentsReporting.setSize(pagedModel.getPage().getSize());
        mappedPaymentsReporting.setNumber(pagedModel.getPage().getNumber());
        mappedPaymentsReporting.setTotalElements(pagedModel.getPage().getTotalElements());
      }
    }
    return mappedPaymentsReporting;
  }

}
