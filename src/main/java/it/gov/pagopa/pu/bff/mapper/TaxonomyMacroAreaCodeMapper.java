package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTOEmbedded;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyMacroAreaCodeMapper {
  public List<TaxonomyMacroAreaCodeDTO> map(CollectionModelTaxonomyMacroAreaCodeDTOEmbedded input) {
    return input.getTaxonomyMacroAreaCodeDTOes().stream()
      .map(taxonomyCodeDTO -> {
        TaxonomyMacroAreaCodeDTO dto = new TaxonomyMacroAreaCodeDTO();
        dto.setOrganizationTypeDescription(taxonomyCodeDTO.getOrganizationTypeDescription());
        dto.setOrganizationType(taxonomyCodeDTO.getOrganizationType());
        dto.setMacroAreaCode(taxonomyCodeDTO.getMacroAreaCode());
        dto.setMacroAreaName(taxonomyCodeDTO.getMacroAreaName());
        dto.setMacroAreaDescription(taxonomyCodeDTO.getMacroAreaDescription());
        return dto;
      })
      .toList();
  }
}
