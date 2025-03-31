package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class ReceiptViewMapper {

  public PagedReceiptView mapToPagedReceiptView(PagedModelReceiptView pagedModel) {
    PagedReceiptView mappedReceipts = new PagedReceiptView();

    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(pagedModel.getEmbedded().getReceiptViews())) {
        mappedReceipts.setContent(pagedModel.getEmbedded().getReceiptViews());
      } else {
        mappedReceipts.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        mappedReceipts.setTotalPages(pagedModel.getPage().getTotalPages());
        mappedReceipts.setSize(pagedModel.getPage().getSize());
        mappedReceipts.setNumber(pagedModel.getPage().getNumber());
        mappedReceipts.setTotalElements(pagedModel.getPage().getTotalElements());
      }
    }
    return mappedReceipts;
  }

}
