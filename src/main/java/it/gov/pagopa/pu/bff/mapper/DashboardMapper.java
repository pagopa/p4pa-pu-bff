package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DashboardDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDashboardDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardMapper {

  public PagedDashboardDTO mapToPagedDashboardDTO(
    PagedInstallmentView installments) {
    if (installments == null) {
      return null;
    }

    List<DashboardDTO> content = installments.getContent().stream().map(
      this::mapToDashboardDTO).toList();

    return PagedDashboardDTO.builder()
      .content(content)
      .size(installments.getSize())
      .totalElements(installments.getTotalElements())
      .totalPages(installments.getTotalPages())
      .number(installments.getNumber())
      .build();
  }

  public DashboardDTO mapToDashboardDTO(InstallmentView installment) {
    if (installment == null) {
      return null;
    }

    DashboardDTO dashboardDTO = new DashboardDTO();
    dashboardDTO.setHasInstallment(true);
    dashboardDTO.setInstallmentId(installment.getInstallmentId());
    dashboardDTO.setHasDebtPosition(true);
    dashboardDTO.setDebtPositionId(installment.getDebtPositionId());

    if (installment.getReceiptId() != null) {
      dashboardDTO.setHasReceipt(true);
      dashboardDTO.setReceiptId(installment.getReceiptId());
    }

    return dashboardDTO;
  }
}
