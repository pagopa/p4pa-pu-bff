package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.PaymentOptionsExtendedDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PaymentOptionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentOptionsMapper {

  public List<PaymentOptionsExtendedDTO> mapToExtended(List<PaymentOptionDTO> paymentOptions) {
    return paymentOptions.stream()
      .<PaymentOptionsExtendedDTO>map(po -> {
        PersonDTO debtor = (!po.getInstallments().isEmpty())
          ? po.getInstallments().getFirst().getDebtor()
          : null;

        return PaymentOptionsExtendedDTO.builder()
          .paymentOptionId(po.getPaymentOptionId())
          .debtPositionId(po.getDebtPositionId())
          .totalAmountCents(po.getTotalAmountCents())
          .status(po.getStatus())
          .description(po.getDescription())
          .paymentOptionType(po.getPaymentOptionType())
          .paymentOptionIndex(po.getPaymentOptionIndex())
          .creationDate(po.getCreationDate())
          .updateDate(po.getUpdateDate())
          .updateOperatorExternalId(po.getUpdateOperatorExternalId())
          .updateTraceId(po.getUpdateTraceId())
          .installments(po.getInstallments())
          .debtor(debtor)
          .build();
      })
      .toList();
  }
}
