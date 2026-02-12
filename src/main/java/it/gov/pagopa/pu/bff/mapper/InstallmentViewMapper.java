package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedInstallmentsView;
import org.springframework.stereotype.Component;

@Component
public class InstallmentViewMapper {

  public PagedInstallmentView mapToPagedInstallmentView(PagedInstallmentsView pagedModel) {
    PagedInstallmentView mappedInstallments = new PagedInstallmentView();

    if (pagedModel != null) {
      mappedInstallments.setContent(pagedModel.getContent());
      mappedInstallments.setTotalPages(pagedModel.getTotalPages());
      mappedInstallments.setSize(pagedModel.getSize());
      mappedInstallments.setNumber(pagedModel.getNumber());
      mappedInstallments.setTotalElements(pagedModel.getTotalElements());
    }
    return mappedInstallments;
  }

}
