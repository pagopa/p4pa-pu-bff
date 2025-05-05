package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypePatchRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeMapper {

  public DebtPositionTypeDetailDTO mapToDebtPositionTypeDetailDTO(
    DebtPositionType debtPositionType, Taxonomy taxonomy) {
    if (debtPositionType == null || taxonomy == null) {
      return null;
    }

    return DebtPositionTypeDetailDTO.builder()
      .debtPositionTypeId(debtPositionType.getDebtPositionTypeId())
      .code(debtPositionType.getCode())
      .description(debtPositionType.getDescription())
      .organizationTypeDescription(taxonomy.getOrganizationTypeDescription())
      .macroAreaName(taxonomy.getMacroAreaName())
      .serviceType(taxonomy.getServiceType())
      .collectionReason(taxonomy.getCollectionReason())
      .taxonomyCode(debtPositionType.getTaxonomyCode())
      .flagAnonymousFiscalCode(debtPositionType.getFlagAnonymousFiscalCode())
      .flagMandatoryDueDate(debtPositionType.getFlagMandatoryDueDate())
      .flagNotifyIo(debtPositionType.getFlagNotifyIo())
      .ioTemplateMessage(debtPositionType.getIoTemplateMessage())
      .ioTemplateSubject(debtPositionType.getIoTemplateSubject())
      .build();
  }

  public DebtPositionTypeRequestBody mapToDebtPositionTypeRequestBody(
    DebtPositionTypePatchRequestBody debtPositionTypePatchRequestBody){
    DebtPositionTypeRequestBody debtPositionTypeRequestBody = new DebtPositionTypeRequestBody();
    debtPositionTypeRequestBody.setFlagAnonymousFiscalCode(debtPositionTypePatchRequestBody.getFlagAnonymousFiscalCode());
    debtPositionTypeRequestBody.setFlagMandatoryDueDate(debtPositionTypePatchRequestBody.getFlagMandatoryDueDate());
    debtPositionTypeRequestBody.setFlagNotifyIo(debtPositionTypePatchRequestBody.getFlagNotifyIo());
    debtPositionTypeRequestBody.setIoTemplateMessage(debtPositionTypePatchRequestBody.getIoTemplateMessage());
    debtPositionTypeRequestBody.setIoTemplateSubject(debtPositionTypePatchRequestBody.getIoTemplateSubject());
    return debtPositionTypeRequestBody;
  }
}
