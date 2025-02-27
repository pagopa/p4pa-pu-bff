package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DebtPositionMapper {

  private final PersonDTOMapper personDTOMapper;
  private final PaymentOptionMapper paymentOptionMapper;
  private static final String MULTI_DEBTOR_NAME = "CO-OBBLIGATO";

  public DebtPositionMapper(PersonDTOMapper personDTOMapper,
    PaymentOptionMapper paymentOptionMapper) {
    this.personDTOMapper = personDTOMapper;
    this.paymentOptionMapper = paymentOptionMapper;
  }

  public DebtPositionDetailDTO mapToDebtPositionDetailDTO(DebtPositionDTO debtPosition, DebtPositionTypeOrg debtPositionTypeOrg){
      DebtPositionDetailDTO debtPositionDetailDTO = DebtPositionDetailDTO.builder()
      		.debtPositionTypeOrgDescription(debtPositionTypeOrg!=null?debtPositionTypeOrg.getDescription():null)
      		.debtPositionTypeOrgCode(debtPositionTypeOrg!=null?debtPositionTypeOrg.getCode():null)
      		.iupd(debtPosition.getIupdOrg())
      		.status(debtPosition.getStatus())
      		.paymentOptions(debtPosition.getPaymentOptions().stream()
            .map(paymentOptionMapper::mapToPaymentOptionDTO).toList())
      		.build();
      debtPositionDetailDTO.setDebtor(buildDebtor(debtPosition));
      return debtPositionDetailDTO;
  }

  private PersonDTO buildDebtor(DebtPositionDTO debtPosition) {
    PersonDTO debtor = null;
    if(Boolean.TRUE.equals(debtPosition.getMultiDebtor())){
      debtor = PersonDTO.builder()
        .fullName(MULTI_DEBTOR_NAME)
        .build();
    }else if(!CollectionUtils.isEmpty(debtPosition.getPaymentOptions())
      && !CollectionUtils.isEmpty(debtPosition.getPaymentOptions().getFirst().getInstallments())){
      debtor = personDTOMapper.mapToPersonDTO(debtPosition.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor());
    }
    return debtor;
  }
}
