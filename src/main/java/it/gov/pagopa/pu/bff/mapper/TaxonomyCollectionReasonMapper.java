package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTOEmbedded;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyCollectionReasonMapper {
  public List<TaxonomyCollectionReasonDTO> map(CollectionModelTaxonomyCollectionReasonDTOEmbedded input) {
    return input.getTaxonomyCollectionReasonDTOes().stream()
      .map(taxonomyCodeDTO -> {
        TaxonomyCollectionReasonDTO dto = new TaxonomyCollectionReasonDTO();
        dto.setOrganizationTypeDescription(taxonomyCodeDTO.getOrganizationTypeDescription());
        dto.setOrganizationType(taxonomyCodeDTO.getOrganizationType());
        dto.setMacroAreaCode(taxonomyCodeDTO.getMacroAreaCode());
        dto.setMacroAreaName(taxonomyCodeDTO.getMacroAreaName());
        dto.setMacroAreaDescription(taxonomyCodeDTO.getMacroAreaDescription());
        dto.setServiceTypeCode(taxonomyCodeDTO.getServiceTypeCode());
        dto.setServiceType(taxonomyCodeDTO.getServiceType());
        dto.setServiceTypeDescription(taxonomyCodeDTO.getServiceTypeDescription());
        dto.setCollectionReason(taxonomyCodeDTO.getCollectionReason());
        return dto;
      })
      .toList();
  }

}
