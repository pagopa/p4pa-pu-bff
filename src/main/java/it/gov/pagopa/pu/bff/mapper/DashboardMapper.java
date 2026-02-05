package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.classification.dto.generated.Classification;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassification;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelClassificationEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentViewDTO;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DashboardMapper {

  public DashboardByFc mapToDashboardByFc(PagedInstallmentView pagedInstallments) {
    List<InstallmentViewDTO> installments = Optional.ofNullable(pagedInstallments)
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
        .map(InstallmentViewDTO::getDebtPositionId)
        .distinct()
        .toList();
      dashboard.setHasDebtPosition(!distinctDebtPositionIds.isEmpty());

      if (distinctDebtPositionIds.size() == 1) {
        dashboard.setDebtPositionId(distinctDebtPositionIds.getFirst());
      }

      List<Long> distinctReceiptIds = installments.stream()
        .map(InstallmentViewDTO::getReceiptId)
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

  public DashboardByIuv mapToDashboardByIuv(PagedInstallmentView pagedInstallments, PagedModelClassification pagedClassifications) {
    DashboardByIuv dashboardByIuv = new DashboardByIuv();
    DashboardByFc dashboardByFc = mapToDashboardByFc(pagedInstallments);

    dashboardByIuv.setHasInstallment(dashboardByFc.getHasInstallment());
    dashboardByIuv.setInstallmentId(dashboardByFc.getInstallmentId());
    dashboardByIuv.setHasDebtPosition(dashboardByFc.getHasDebtPosition());
    dashboardByIuv.setDebtPositionId(dashboardByFc.getDebtPositionId());
    dashboardByIuv.setHasReceipt(dashboardByFc.getHasReceipt());
    dashboardByIuv.setReceiptId(dashboardByFc.getReceiptId());

    List<Classification> classifications = Optional.ofNullable(pagedClassifications)
      .map(PagedModelClassification::getEmbedded)
      .map(PagedModelClassificationEmbedded::getClassifications)
      .orElseGet(Collections::emptyList);

    if (classifications.isEmpty()) {
      dashboardByIuv.setHasClassification(false);
      dashboardByIuv.setHasIuf(false);
    } else {
      dashboardByIuv.setHasClassification(true);

      if (classifications.size() == 1) {
        dashboardByIuv.setClassificationId(classifications.getFirst().getClassificationId());
      }

      List<String> distinctIufs = classifications.stream()
        .map(Classification::getIuf)
        .filter(Objects::nonNull)
        .distinct()
        .toList();

      dashboardByIuv.setHasIuf(!distinctIufs.isEmpty());

      if (distinctIufs.size() == 1) {
        dashboardByIuv.setIuf(distinctIufs.getFirst());
      }
    }

    return dashboardByIuv;
  }

  public DashboardByIuf mapToDashboardByIuf(PagedModelClassification pagedClassifications) {
    DashboardByIuf dashboardByIuf = new DashboardByIuf();
    DashboardByIuv dashboardByIuv = mapToDashboardByIuv(null, pagedClassifications);

    dashboardByIuf.setHasIuf(dashboardByIuv.getHasIuf());
    dashboardByIuf.setIuf(dashboardByIuv.getIuf());
    dashboardByIuf.setHasClassification(dashboardByIuv.getHasClassification());
    dashboardByIuf.setClassificationId(dashboardByIuv.getClassificationId());

    List<Classification> classifications = Optional.ofNullable(pagedClassifications)
      .map(PagedModelClassification::getEmbedded)
      .map(PagedModelClassificationEmbedded::getClassifications)
      .orElseGet(Collections::emptyList);

    if (classifications.isEmpty()) {
      dashboardByIuf.setHasTreasury(false);
    } else {
      List<String> distinctTreasuryIds = classifications.stream()
        .map(Classification::getTreasuryId)
        .filter(Objects::nonNull)
        .distinct()
        .toList();

      dashboardByIuf.setHasTreasury(!distinctTreasuryIds.isEmpty());

      if (distinctTreasuryIds.size() == 1) {
        dashboardByIuf.setTreasuryId(distinctTreasuryIds.getFirst());
      }
    }

    return dashboardByIuf;
  }
}
