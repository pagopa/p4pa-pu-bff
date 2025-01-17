package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyCollectionReasonMapper {
  public TaxonomyCollectionReasonDTO map(
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCollectionReasonDTO input) {
    TaxonomyCollectionReasonDTO dto = new TaxonomyCollectionReasonDTO();
    dto.setOrganizationTypeDescription(input.getOrganizationType()+". "+input.getOrganizationTypeDescription());
    dto.setOrganizationType(input.getOrganizationType());
    dto.setMacroAreaCode(input.getMacroAreaCode());
    dto.setMacroAreaName(input.getMacroAreaCode()+". "+input.getMacroAreaName());
    dto.setMacroAreaDescription(input.getMacroAreaDescription());
    dto.setServiceTypeCode(input.getServiceTypeCode());
    dto.setServiceType(input.getServiceTypeCode()+". "+input.getServiceType());
    dto.setServiceTypeDescription(input.getServiceTypeDescription());
    dto.setCollectionReason(input.getCollectionReason());
    return dto;
  }

}
