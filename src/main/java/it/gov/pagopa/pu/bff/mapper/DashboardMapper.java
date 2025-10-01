package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.PagedDashboardDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardMapper {

  public PagedDashboardDTO mapToPagedDashboardByFcDTO(
    PagedInstallmentView installments) {
    if (installments == null) {
      return null;
    }

    List<DashboardByFc> content = installments.getContent().stream().map(
      this::mapToDashboardByFc).toList();

    return PagedDashboardDTO.builder()
      .content(content)
      .size(installments.getSize())
      .totalElements(installments.getTotalElements())
      .totalPages(installments.getTotalPages())
      .number(installments.getNumber())
      .build();
  }

  public DashboardByFc mapToDashboardByFc(InstallmentView installment) {
    DashboardByFc out = new DashboardByFc();

    if (installment != null) {
      out.setHasInstallment(true);
      out.setInstallmentId(installment.getInstallmentId());
      out.setHasDebtPosition(true);
      out.setDebtPositionId(installment.getDebtPositionId());

      if (installment.getReceiptId() != null) {
        out.setHasReceipt(true);
        out.setReceiptId(installment.getReceiptId());
      }
    }

    return out;
  }
}
