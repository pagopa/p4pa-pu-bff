package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.InstallmentDTO;
import org.springframework.stereotype.Component;

@Component
public class InstallmentMapper {
  public InstallmentDTO mapToInstallmentDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDTO installmentDTO){
      return InstallmentDTO.builder()
      		.installmentId(installmentDTO.getInstallmentId())
      		.paymentOptionId(installmentDTO.getPaymentOptionId())
      		.status(installmentDTO.getStatus())
      		.iuv(installmentDTO.getIuv())
      		.dueDate(installmentDTO.getDueDate())
      		.amountCents(installmentDTO.getAmountCents())
      		.remittanceInformation(installmentDTO.getRemittanceInformation())
      		.build();
  }
}
