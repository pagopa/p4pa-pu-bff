package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

@Component
public class DebtPositionMapper {
  private final PaymentOptionsMapper paymentOptionsMapper;

  private static final String MULTI_DEBTOR_NAME = "CO-OBBLIGATO";

  public DebtPositionMapper(PaymentOptionsMapper paymentOptionsMapper) {
    this.paymentOptionsMapper = paymentOptionsMapper;
  }

  public DebtPositionDetailDTO mapToDebtPositionDetailDTO(DebtPositionDTO debtPosition, DebtPositionTypeOrg debtPositionTypeOrg){
      DebtPositionDetailDTO debtPositionDetailDTO = DebtPositionDetailDTO.builder()
      		.debtPositionTypeOrgDescription(debtPositionTypeOrg!=null?debtPositionTypeOrg.getDescription():null)
      		.debtPositionTypeOrgCode(debtPositionTypeOrg!=null?debtPositionTypeOrg.getCode():null)
          .debtPositionOrigin(debtPosition.getDebtPositionOrigin())
      		.iupd(debtPosition.getIupdOrg())
      		.status(debtPosition.getStatus())
          .description(debtPosition.getDescription())
      		.build();
      List<PaymentOptionDTO> paymentOptions = sortInstallments(debtPosition.getPaymentOptions());
      debtPositionDetailDTO.setPaymentOptions(paymentOptionsMapper.mapToExtended(paymentOptions));
      debtPositionDetailDTO.setDebtor(buildDebtor(debtPosition));
      return debtPositionDetailDTO;
  }

  private it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO buildDebtor(DebtPositionDTO debtPosition) {
    PersonDTO debtor = null;
    if(Boolean.TRUE.equals(debtPosition.getMultiDebtor())){
      debtor = it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO.builder()
        .fullName(MULTI_DEBTOR_NAME)
        .entityType(PersonEntityType.F)
        .fiscalCode("")
        .build();
    }else if(!CollectionUtils.isEmpty(debtPosition.getPaymentOptions())
      && !CollectionUtils.isEmpty(debtPosition.getPaymentOptions().getFirst().getInstallments())){
      debtor = debtPosition.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor();
    }
    return debtor;
  }

  private List<PaymentOptionDTO> sortInstallments(List<PaymentOptionDTO> paymentOptions) {
    paymentOptions
      .forEach(po -> po.setInstallments(po.getInstallments()
        .stream()
        .sorted(Comparator.comparing(InstallmentDTO::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())))
        .toList()));
    return paymentOptions;
  }
}
