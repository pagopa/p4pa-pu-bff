package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingWithReceiptViewRow;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class PaymentsReportingMapper {

  public PagedPaymentsReportingWithReceiptViewRow mapToPagedPaymentsReportingWithReceiptView(
    PagedModelPaymentsReportingWithReceiptView pagedModel) {
    PagedPaymentsReportingWithReceiptViewRow pagedPaymentsReportingWithReceiptView = new PagedPaymentsReportingWithReceiptViewRow();
    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(
        pagedModel.getEmbedded().getPaymentsReportingWithReceiptViews())) {
        pagedPaymentsReportingWithReceiptView.setContent(
          pagedModel.getEmbedded().getPaymentsReportingWithReceiptViews());
      } else {
        pagedPaymentsReportingWithReceiptView.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        pagedPaymentsReportingWithReceiptView.setTotalPages(
          pagedModel.getPage().getTotalPages());
        pagedPaymentsReportingWithReceiptView.setSize(pagedModel.getPage().getSize());
        pagedPaymentsReportingWithReceiptView.setNumber(pagedModel.getPage().getNumber());
        pagedPaymentsReportingWithReceiptView.setTotalElements(
          pagedModel.getPage().getTotalElements());
      }
    }
    return pagedPaymentsReportingWithReceiptView;
  }

  public PaymentsReportingDetailDTO mapToPaymentsReportingDetailDTO(
    PaymentsReporting paymentsReporting, ReceiptDetailDTO receiptDetailDTO) {
    if (paymentsReporting == null) {
      return null;
    }

    PaymentsReportingDetailDTO dto = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .status(InstallmentStatus.REPORTED)
      .build();

    if (receiptDetailDTO != null) {
      dto.setIud(receiptDetailDTO.getIud());
      dto.setDebtPositionTypeOrgDescription(
        receiptDetailDTO.getDebtPositionTypeOrgDescription());
      dto.setPaymentDateTime(receiptDetailDTO.getPaymentDateTime());
      dto.setPspCompanyName(receiptDetailDTO.getPspCompanyName());
      dto.setRemittanceInformation(receiptDetailDTO.getRemittanceInformation());
      dto.setDebtor(receiptDetailDTO.getDebtor());
    }

    return dto;
  }
}
