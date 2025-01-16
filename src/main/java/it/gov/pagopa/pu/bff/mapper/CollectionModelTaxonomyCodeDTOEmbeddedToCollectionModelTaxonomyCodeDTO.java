package it.gov.pagopa.pu.bff.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.exception.UnparsableEntityException;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCodeDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CollectionModelTaxonomyCodeDTOEmbeddedToCollectionModelTaxonomyCodeDTO {

  private final ObjectMapper objectMapper;

  public CollectionModelTaxonomyCodeDTOEmbeddedToCollectionModelTaxonomyCodeDTO(ObjectMapper objectMapper){
    this.objectMapper = objectMapper;
  }

  public CollectionModelTaxonomyCodeDTO map(
    CollectionModelTaxonomyCodeDTOEmbedded input){
    try{
      List<TaxonomyCodeDTO> modifiedList = input.getTaxonomyCodeDTOes().stream()
        .map(dto -> {
          dto.setOrganizationTypeDescription(dto.getOrganizationType() + ". " + dto.getOrganizationTypeDescription());
          dto.setMacroAreaDescription(dto.getMacroAreaCode()+". "+dto.getMacroAreaName());
          dto.setServiceTypeDescription(dto.getServiceTypeCode()+". "+dto.getServiceType());
          return dto;
        }).toList();
      CollectionModelTaxonomyCodeDTOEmbedded modifiedEmbedded = new CollectionModelTaxonomyCodeDTOEmbedded();
      modifiedEmbedded.setTaxonomyCodeDTOes(modifiedList);
      String result = objectMapper.writeValueAsString(modifiedEmbedded);
      return objectMapper.readValue(result, CollectionModelTaxonomyCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new UnparsableEntityException(e.getMessage());
    }
  }

}
