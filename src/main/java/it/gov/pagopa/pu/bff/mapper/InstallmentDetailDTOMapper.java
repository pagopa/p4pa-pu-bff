package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import org.springframework.stereotype.Component;

@Component
public class InstallmentDetailDTOMapper {

  public InstallmentDetailDTO mapToInstallmentDetailDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO) {
    if (installmentDetailDTO == null) {
      return null;
    }
    InstallmentDetailDTO.StatusEnum status;
    if (installmentDetailDTO.getPayer() == null &&
      installmentDetailDTO.getPaymentDateTime() == null &&
      installmentDetailDTO.getIud() == null &&
      installmentDetailDTO.getIur() == null &&
      installmentDetailDTO.getPspCompanyName() == null) {
      status = switch (installmentDetailDTO.getStatus()) {
        case UNPAID, EXPIRED, CANCELLED, INVALID, TO_SYNC, DRAFT ->
          InstallmentDetailDTO.StatusEnum.fromValue(installmentDetailDTO.getStatus().getValue());
        default -> InstallmentDetailDTO.StatusEnum.UNPAID;
      };
    } else {
      status = switch (installmentDetailDTO.getStatus()) {
        case PAID, REPORTED ->
          InstallmentDetailDTO.StatusEnum.fromValue(installmentDetailDTO.getStatus().getValue());
        default -> InstallmentDetailDTO.StatusEnum.PAID;
      };
    }
    return InstallmentDetailDTO.builder()
      .installmentId(installmentDetailDTO.getInstallmentId())
      .receiptId(installmentDetailDTO.getReceiptId())
      .paymentOptionId(installmentDetailDTO.getPaymentOptionId())
      .status(status)
      .iuv(installmentDetailDTO.getIuv())
      .amountCents(installmentDetailDTO.getAmountCents())
      .dueDate(installmentDetailDTO.getDueDate())
      .debtor(installmentDetailDTO.getDebtor())
      .debtPositionTypeOrgDescription(installmentDetailDTO.getDebtPositionTypeOrgDescription())
      .debtPositionDescription(installmentDetailDTO.getDebtPositionDescription())
      .debtPositionId(installmentDetailDTO.getDebtPositionId())
      .paymentDateTime(installmentDetailDTO.getPaymentDateTime())
      .payer(installmentDetailDTO.getPayer())
      .pspCompanyName(installmentDetailDTO.getPspCompanyName())
      .iud(installmentDetailDTO.getIud())
      .iur(installmentDetailDTO.getIur())
      .build();
  }

}
