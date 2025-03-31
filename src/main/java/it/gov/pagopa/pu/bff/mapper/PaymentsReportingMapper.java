package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class PaymentsReportingMapper {

  public PagedPaymentsReportingRow mapToPagedPaymentsReporting(
    PagedModelPaymentsReporting pagedModel) {
    PagedPaymentsReportingRow pagedPaymentsReporting = new PagedPaymentsReportingRow();
    if (pagedModel != null) {
      if (pagedModel.getEmbedded() != null && !CollectionUtils.isEmpty(
        pagedModel.getEmbedded().getPaymentsReportings())) {
        pagedPaymentsReporting.setContent(
          pagedModel.getEmbedded().getPaymentsReportings());
      } else {
        pagedPaymentsReporting.setContent(Collections.emptyList());
      }
      if (pagedModel.getPage() != null) {
        pagedPaymentsReporting.setTotalPages(
          pagedModel.getPage().getTotalPages());
        pagedPaymentsReporting.setSize(pagedModel.getPage().getSize());
        pagedPaymentsReporting.setNumber(pagedModel.getPage().getNumber());
        pagedPaymentsReporting.setTotalElements(
          pagedModel.getPage().getTotalElements());
      }
    }
    return pagedPaymentsReporting;
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
