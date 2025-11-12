package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class ReceiptDetailDTOMapper {

  public ReceiptDetailDTO mapToReceiptDetailDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO) {
    if (receiptDetailDTO == null) {
      return null;
    }
    return ReceiptDetailDTO.builder()
      .receiptId(receiptDetailDTO.getReceiptId())
      .iuv(receiptDetailDTO.getIuv())
      .nav(receiptDetailDTO.getNav())
      .paymentAmountCents(receiptDetailDTO.getPaymentAmountCents())
      .remittanceInformation(receiptDetailDTO.getRemittanceInformation())
      .debtPositionTypeOrgDescription(receiptDetailDTO.getDebtPositionTypeOrgDescription())
      .debtor(receiptDetailDTO.getDebtor())
      .paymentDateTime(receiptDetailDTO.getPaymentDateTime())
      .pspCompanyName(receiptDetailDTO.getPspCompanyName())
      .iud(receiptDetailDTO.getIud())
      .iur(receiptDetailDTO.getIur())
      .feeCents(receiptDetailDTO.getFeeCents())
      .notificationFeeCents(receiptDetailDTO.getNotificationFeeCents())
      .build();
  }

}
