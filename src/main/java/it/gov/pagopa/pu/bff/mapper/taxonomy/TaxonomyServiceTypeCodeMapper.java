package it.gov.pagopa.pu.bff.mapper.taxonomy;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyServiceTypeCodeMapper {
  public TaxonomyServiceTypeCodeDTO map(
    it.gov.pagopa.pu.organization.dto.generated.TaxonomyServiceTypeCodeDTO input) {
    TaxonomyServiceTypeCodeDTO dto = new TaxonomyServiceTypeCodeDTO();
    dto.setOrganizationTypeDescription(input.getOrganizationType()+". "+input.getOrganizationTypeDescription());
    dto.setOrganizationType(input.getOrganizationType());
    dto.setMacroAreaCode(input.getMacroAreaCode());
    dto.setMacroAreaName(input.getMacroAreaCode()+". "+input.getMacroAreaName());
    dto.setMacroAreaDescription(input.getMacroAreaDescription());
    dto.setServiceTypeCode(input.getServiceTypeCode());
    dto.setServiceType(input.getServiceTypeCode()+". "+input.getServiceType());
    dto.setServiceTypeDescription(input.getServiceTypeDescription());
    return dto;
  }

}
