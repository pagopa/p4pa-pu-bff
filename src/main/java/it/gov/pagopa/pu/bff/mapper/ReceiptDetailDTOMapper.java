package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class ReceiptDetailDTOMapper {

  public ReceiptDetailDTO mapToReceiptDetailDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO) {
    return ReceiptDetailDTO.builder()
      .receiptId(receiptDetailDTO.getReceiptId())
      .iuv(receiptDetailDTO.getIuv())
      .paymentAmountCents(receiptDetailDTO.getPaymentAmountCents())
      .remittanceInformation(receiptDetailDTO.getRemittanceInformation())
      .debtPositionDescription(receiptDetailDTO.getDebtPositionDescription())
      .debtorFullName(receiptDetailDTO.getDebtor()!=null?receiptDetailDTO.getDebtor().getFullName():null)
      .debtorFiscalCode(receiptDetailDTO.getDebtor()!=null?receiptDetailDTO.getDebtor().getFiscalCode():null)
      .payerFullName(receiptDetailDTO.getPayer()!=null?receiptDetailDTO.getPayer().getFullName():null)
      .payerFiscalCode(receiptDetailDTO.getPayer()!=null?receiptDetailDTO.getPayer().getFiscalCode():null)
      .paymentDateTime(receiptDetailDTO.getPaymentDateTime())
      .pspCompanyName(receiptDetailDTO.getPspCompanyName())
      .iud(receiptDetailDTO.getIud())
      .iur(receiptDetailDTO.getIur())
      .build();
  }

}
