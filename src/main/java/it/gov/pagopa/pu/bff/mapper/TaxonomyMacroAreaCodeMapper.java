package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyMacroAreaCodeMapper {
  public TaxonomyMacroAreaCodeDTO map(
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyMacroAreaCodeDTO input) {
    TaxonomyMacroAreaCodeDTO dto = new TaxonomyMacroAreaCodeDTO();
    dto.setOrganizationTypeDescription(input.getOrganizationType()+". "+input.getOrganizationTypeDescription());
    dto.setOrganizationType(input.getOrganizationType());
    dto.setMacroAreaCode(input.getMacroAreaCode());
    dto.setMacroAreaName(input.getMacroAreaCode()+". "+input.getMacroAreaName());
    dto.setMacroAreaDescription(input.getMacroAreaDescription());
    return dto;
  }
}
