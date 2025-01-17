package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyCodeMapper {

  public TaxonomyCodeDTO map(
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCodeDTO input) {
    TaxonomyCodeDTO dto = new TaxonomyCodeDTO();
    dto.setOrganizationTypeDescription(input.getOrganizationType()+". "+input.getOrganizationTypeDescription());
    dto.setOrganizationType(input.getOrganizationType());
    dto.setMacroAreaCode(input.getMacroAreaCode());
    dto.setMacroAreaName(input.getMacroAreaCode()+". "+input.getMacroAreaName());
    dto.setMacroAreaDescription(input.getMacroAreaDescription());
    dto.setServiceTypeCode(input.getServiceTypeCode());
    dto.setServiceType(input.getServiceTypeCode()+". "+input.getServiceType());
    dto.setServiceTypeDescription(input.getServiceTypeDescription());
    dto.setCollectionReason(input.getCollectionReason());
    dto.setTaxonomyCode(input.getTaxonomyCode());
    return dto;
  }

}
