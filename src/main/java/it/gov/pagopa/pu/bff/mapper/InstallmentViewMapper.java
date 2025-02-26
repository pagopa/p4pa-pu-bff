package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class InstallmentViewMapper {

  public PagedInstallmentView mapToPagedInstallmentView(PagedModelInstallmentView pagedModel) {
    PagedInstallmentView mappedInstallments = new PagedInstallmentView();

    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(pagedModel.getEmbedded().getInstallmentViews())) {
        mappedInstallments.setContent(pagedModel.getEmbedded().getInstallmentViews());
      } else {
        mappedInstallments.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        mappedInstallments.setTotalPages(pagedModel.getPage().getTotalPages());
        mappedInstallments.setSize(pagedModel.getPage().getSize());
        mappedInstallments.setNumber(pagedModel.getPage().getNumber());
        mappedInstallments.setTotalElements(pagedModel.getPage().getTotalElements());
      }
    }
    return mappedInstallments;
  }

}
