package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyOrganizationTypeMapper {
  public TaxonomyOrganizationTypeDTO map(
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyOrganizationTypeDTO input) {
    TaxonomyOrganizationTypeDTO dto = new TaxonomyOrganizationTypeDTO();
    dto.setOrganizationTypeDescription(input.getOrganizationType()+". "+input.getOrganizationTypeDescription());
    dto.setOrganizationType(input.getOrganizationType());
    return dto;
  }

}
