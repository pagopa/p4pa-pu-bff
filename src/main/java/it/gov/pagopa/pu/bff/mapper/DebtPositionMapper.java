package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.EntityTypeEnum;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class DebtPositionMapper {
  private static final String MULTI_DEBTOR_NAME = "CO-OBBLIGATO";

  public DebtPositionDetailDTO mapToDebtPositionDetailDTO(DebtPositionDTO debtPosition, DebtPositionTypeOrg debtPositionTypeOrg){
      DebtPositionDetailDTO debtPositionDetailDTO = DebtPositionDetailDTO.builder()
      		.debtPositionTypeOrgDescription(debtPositionTypeOrg!=null?debtPositionTypeOrg.getDescription():null)
      		.debtPositionTypeOrgCode(debtPositionTypeOrg!=null?debtPositionTypeOrg.getCode():null)
      		.iupd(debtPosition.getIupdOrg())
      		.status(debtPosition.getStatus())
      		.paymentOptions(debtPosition.getPaymentOptions())
      		.build();
      debtPositionDetailDTO.setDebtor(buildDebtor(debtPosition));
      return debtPositionDetailDTO;
  }

  private it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO buildDebtor(DebtPositionDTO debtPosition) {
    PersonDTO debtor = null;
    if(Boolean.TRUE.equals(debtPosition.getMultiDebtor())){
      debtor = it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO.builder()
        .fullName(MULTI_DEBTOR_NAME)
        .entityType(EntityTypeEnum.F)
        .fiscalCode("")
        .build();
    }else if(!CollectionUtils.isEmpty(debtPosition.getPaymentOptions())
      && !CollectionUtils.isEmpty(debtPosition.getPaymentOptions().getFirst().getInstallments())){
      debtor = debtPosition.getPaymentOptions().getFirst().getInstallments().getFirst().getDebtor();
    }
    return debtor;
  }
}
