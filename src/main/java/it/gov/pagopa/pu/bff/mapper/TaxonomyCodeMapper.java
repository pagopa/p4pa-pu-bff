package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTOEmbedded;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaxonomyCodeMapper {

  public List<TaxonomyCodeDTO> map(CollectionModelTaxonomyCodeDTOEmbedded input) {
    return input.getTaxonomyCodeDTOes().stream().map(taxonomyCodeDTO -> {
      TaxonomyCodeDTO element = new TaxonomyCodeDTO();
      element.setOrganizationTypeDescription(taxonomyCodeDTO.getOrganizationTypeDescription());
      element.setOrganizationType(taxonomyCodeDTO.getOrganizationType());
      element.setMacroAreaCode(taxonomyCodeDTO.getMacroAreaCode());
      element.setMacroAreaName(taxonomyCodeDTO.getMacroAreaName());
      element.setMacroAreaDescription(taxonomyCodeDTO.getMacroAreaDescription());
      element.setServiceTypeCode(taxonomyCodeDTO.getServiceTypeCode());
      element.setServiceType(taxonomyCodeDTO.getServiceType());
      element.setServiceTypeDescription(taxonomyCodeDTO.getServiceTypeDescription());
      element.setCollectionReason(taxonomyCodeDTO.getCollectionReason());
      element.setTaxonomyCode(taxonomyCodeDTO.getTaxonomyCode());
      return element;
    }).toList();
  }

}
