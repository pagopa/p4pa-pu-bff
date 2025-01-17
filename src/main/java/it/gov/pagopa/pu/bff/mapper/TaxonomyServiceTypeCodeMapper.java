package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTOEmbedded;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyServiceTypeCodeMapper {
  public List<TaxonomyServiceTypeCodeDTO> map(CollectionModelTaxonomyServiceTypeCodeDTOEmbedded input) {
    return input.getTaxonomyServiceTypeCodeDTOes().stream()
      .map(taxonomyCodeDTO -> {
        TaxonomyServiceTypeCodeDTO dto = new TaxonomyServiceTypeCodeDTO();
        dto.setOrganizationTypeDescription(taxonomyCodeDTO.getOrganizationType()+". "+taxonomyCodeDTO.getOrganizationTypeDescription());
        dto.setOrganizationType(taxonomyCodeDTO.getOrganizationType());
        dto.setMacroAreaCode(taxonomyCodeDTO.getMacroAreaCode());
        dto.setMacroAreaName(taxonomyCodeDTO.getMacroAreaName());
        dto.setMacroAreaDescription(taxonomyCodeDTO.getMacroAreaDescription());
        dto.setServiceTypeCode(taxonomyCodeDTO.getServiceTypeCode());
        dto.setServiceType(taxonomyCodeDTO.getServiceType());
        dto.setServiceTypeDescription(taxonomyCodeDTO.getServiceTypeDescription());
        return dto;
      })
      .toList();
  }

}
