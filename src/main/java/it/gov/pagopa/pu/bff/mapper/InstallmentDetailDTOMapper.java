package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDetailDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstallmentDetailDTOMapper {

  private final List<it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum> statusList = List.of(
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.PAID,
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO.StatusEnum.REPORTED);

  public InstallmentDetailDTO mapToInstallmentDetailDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO) {
    if (installmentDetailDTO == null) {
      return null;
    }

    InstallmentDetailDTO installmentDetail = InstallmentDetailDTO.builder()
      .installmentId(installmentDetailDTO.getInstallmentId())
      .receiptId(installmentDetailDTO.getReceiptId())
      .paymentOptionId(installmentDetailDTO.getPaymentOptionId())
      .status(installmentDetailDTO.getStatus())
      .iuv(installmentDetailDTO.getIuv())
      .amountCents(installmentDetailDTO.getAmountCents())
      .dueDate(installmentDetailDTO.getDueDate())
      .debtor(installmentDetailDTO.getDebtor())
      .debtPositionTypeOrgDescription(installmentDetailDTO.getDebtPositionTypeOrgDescription())
      .debtPositionDescription(installmentDetailDTO.getDebtPositionDescription())
      .debtPositionId(installmentDetailDTO.getDebtPositionId())
      .build();

    setPaymentInfo(installmentDetail, installmentDetailDTO);
    return installmentDetail;
  }

  private void setPaymentInfo(InstallmentDetailDTO build,
                              it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO installmentDetailDTO) {
    if (statusList.contains(installmentDetailDTO.getStatus())) {
      build.setPayer(installmentDetailDTO.getPayer());
      build.setPaymentDateTime(installmentDetailDTO.getPaymentDateTime());
      build.setIud(installmentDetailDTO.getIud());
      build.setIur(installmentDetailDTO.getIur());
      build.setPspCompanyName(installmentDetailDTO.getPspCompanyName());
    }
  }

}
