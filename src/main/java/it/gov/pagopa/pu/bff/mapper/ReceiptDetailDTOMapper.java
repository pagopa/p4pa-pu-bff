package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class ReceiptDetailDTOMapper {
  private final PersonDTOMapper personDTOMapper;

  public ReceiptDetailDTOMapper(PersonDTOMapper personDTOMapper) {
    this.personDTOMapper = personDTOMapper;
  }

  public ReceiptDetailDTO mapToReceiptDetailDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO) {
    if (receiptDetailDTO == null) {
      return null;
    }
    return ReceiptDetailDTO.builder()
      .receiptId(receiptDetailDTO.getReceiptId())
      .iuv(receiptDetailDTO.getIuv())
      .paymentAmountCents(receiptDetailDTO.getPaymentAmountCents())
      .remittanceInformation(receiptDetailDTO.getRemittanceInformation())
      .debtPositionTypeOrgDescription(receiptDetailDTO.getDebtPositionTypeOrgDescription())
      .debtor(personDTOMapper.mapToPersonDTO(receiptDetailDTO.getDebtor()))
      .payer(personDTOMapper.mapToPersonDTO(receiptDetailDTO.getPayer()))
      .paymentDateTime(receiptDetailDTO.getPaymentDateTime())
      .pspCompanyName(receiptDetailDTO.getPspCompanyName())
      .iud(receiptDetailDTO.getIud())
      .iur(receiptDetailDTO.getIur())
      .build();
  }

}
