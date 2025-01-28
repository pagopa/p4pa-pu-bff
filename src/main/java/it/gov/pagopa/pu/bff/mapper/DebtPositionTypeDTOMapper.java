package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import org.springframework.stereotype.Component;

@Component
public class DebtPositionTypeDTOMapper {

  public DebtPositionTypeDTO mapToDebtPositionTypeDTO(DebtPositionType debtPositionType) {
    if (debtPositionType == null) {
      return null;
    }

    return DebtPositionTypeDTO.builder()
      .debtPositionTypeId(debtPositionType.getDebtPositionTypeId())
      .brokerId(debtPositionType.getBrokerId())
      .code(debtPositionType.getCode())
      .description(debtPositionType.getDescription())
      .orgType(debtPositionType.getOrgType())
      .macroArea(debtPositionType.getMacroArea())
      .serviceType(debtPositionType.getServiceType())
      .collectingReason(debtPositionType.getCollectingReason())
      .taxonomyCode(debtPositionType.getTaxonomyCode())
      .flagAnonymousFiscalCode(debtPositionType.getFlagAnonymousFiscalCode())
      .flagMandatoryDueDate(debtPositionType.getFlagMandatoryDueDate())
      .flagNotifyIo(debtPositionType.getFlagNotifyIo())
      .ioTemplateMessage(debtPositionType.getIoTemplateMessage())
      .build();
  }

}
