package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum;
import org.springframework.stereotype.Component;

@Component
public class PaymentsReportingDTOMapper {

  public PaymentsReportingDetailDTO mapToPaymentsReportingDetailDTO(PaymentsReporting paymentsReporting, ReceiptDetailDTO receiptDetailDTO) {
    return PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .iud(receiptDetailDTO.getIud())
      .debtPositionTypeOrgDescription(receiptDetailDTO.getDebtPositionTypeOrgDescription())
      .paymentAmountCents(receiptDetailDTO.getPaymentAmountCents())
      .paymentDateTime(receiptDetailDTO.getPaymentDateTime())
      .pspCompanyName(receiptDetailDTO.getPspCompanyName())
      .remittanceInformation(receiptDetailDTO.getRemittanceInformation())
      .debtor(receiptDetailDTO.getDebtor())
      .status(StatusEnum.REPORTED)
      .build();
  }

}
