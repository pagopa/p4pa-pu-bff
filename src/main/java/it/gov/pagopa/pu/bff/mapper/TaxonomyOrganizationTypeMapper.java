package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTOEmbedded;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyOrganizationTypeMapper {
  public List<TaxonomyOrganizationTypeDTO> map(CollectionModelTaxonomyOrganizationTypeDTOEmbedded input) {
    return input.getTaxonomyOrganizationTypeDTOes().stream()
      .map(taxonomyCodeDTO -> {
        TaxonomyOrganizationTypeDTO dto = new TaxonomyOrganizationTypeDTO();
        dto.setOrganizationTypeDescription(taxonomyCodeDTO.getOrganizationTypeDescription());
        dto.setOrganizationType(taxonomyCodeDTO.getOrganizationType());
        return dto;
      })
      .toList();
  }

}
