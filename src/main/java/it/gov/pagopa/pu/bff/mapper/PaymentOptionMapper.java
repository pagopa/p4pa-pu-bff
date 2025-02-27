package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PaymentOptionDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentOptionMapper {
  private final InstallmentMapper installmentMapper;

  public PaymentOptionMapper(InstallmentMapper installmentMapper) {
    this.installmentMapper = installmentMapper;
  }

  public PaymentOptionDTO mapToPaymentOptionDTO(
    it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO paymentOptionDTO){
      return PaymentOptionDTO.builder()
      		.paymentOptionId(paymentOptionDTO.getPaymentOptionId())
      		.totalAmountCents(paymentOptionDTO.getTotalAmountCents())
      		.status(paymentOptionDTO.getStatus())
      		.description(paymentOptionDTO.getDescription())
      		.paymentOptionType(paymentOptionDTO.getPaymentOptionType())
      		.paymentOptionIndex(paymentOptionDTO.getPaymentOptionIndex())
      		.installments(
            paymentOptionDTO.getInstallments().stream()
              .map(installmentMapper::mapToInstallmentDTO).toList())
      		.build();
  }
}
