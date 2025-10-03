package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DashboardByFc;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentView;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DashboardMapper {

  public DashboardByFc mapToDashboardByFc(PagedInstallmentView pagedInstallments) {
    List<InstallmentView> installments = Optional.ofNullable(pagedInstallments)
      .map(PagedInstallmentView::getContent)
      .orElseGet(Collections::emptyList);

    DashboardByFc dashboard = new DashboardByFc();
    boolean hasInstallment = !installments.isEmpty();
    dashboard.setHasInstallment(hasInstallment);

    if (hasInstallment) {
      if (installments.size() == 1) {
        dashboard.setInstallmentId(installments.getFirst().getInstallmentId());
      }

      List<Long> distinctDebtPositionIds = installments.stream()
        .map(InstallmentView::getDebtPositionId)
        .distinct()
        .toList();
      dashboard.setHasDebtPosition(!distinctDebtPositionIds.isEmpty());

      if (distinctDebtPositionIds.size() == 1) {
        dashboard.setDebtPositionId(distinctDebtPositionIds.getFirst());
      }

      List<Long> distinctReceiptIds = installments.stream()
        .map(InstallmentView::getReceiptId)
        .filter(Objects::nonNull)
        .distinct()
        .toList();
      dashboard.setHasReceipt(!distinctReceiptIds.isEmpty());

      if (distinctReceiptIds.size() == 1) {
        dashboard.setReceiptId(distinctReceiptIds.getFirst());
      }
    } else {
      dashboard.setHasDebtPosition(false);
      dashboard.setHasReceipt(false);
    }

    return dashboard;
  }
}
