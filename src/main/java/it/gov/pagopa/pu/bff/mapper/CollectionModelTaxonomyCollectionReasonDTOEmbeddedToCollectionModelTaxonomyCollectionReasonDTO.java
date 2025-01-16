package it.gov.pagopa.pu.bff.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.exception.UnparsableEntityException;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCollectionReasonDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CollectionModelTaxonomyCollectionReasonDTOEmbeddedToCollectionModelTaxonomyCollectionReasonDTO {

  private final ObjectMapper objectMapper;

  public CollectionModelTaxonomyCollectionReasonDTOEmbeddedToCollectionModelTaxonomyCollectionReasonDTO(ObjectMapper objectMapper){
    this.objectMapper = objectMapper;
  }

  public CollectionModelTaxonomyCollectionReasonDTO map(
    CollectionModelTaxonomyCollectionReasonDTOEmbedded input){
    try{
      List<TaxonomyCollectionReasonDTO> modifiedList = input.getTaxonomyCollectionReasonDTOes().stream()
        .map(dto -> {
          dto.setOrganizationTypeDescription(dto.getOrganizationType() + ". " + dto.getOrganizationTypeDescription());
          dto.setMacroAreaDescription(dto.getMacroAreaCode()+". "+dto.getMacroAreaName());
          dto.setServiceTypeDescription(dto.getServiceTypeCode()+". "+dto.getServiceType());
          return dto;
        }).toList();
      CollectionModelTaxonomyCollectionReasonDTOEmbedded modifiedEmbedded = new CollectionModelTaxonomyCollectionReasonDTOEmbedded();
      modifiedEmbedded.setTaxonomyCollectionReasonDTOes(modifiedList);
      String result = objectMapper.writeValueAsString(modifiedEmbedded);
      return objectMapper.readValue(result, CollectionModelTaxonomyCollectionReasonDTO.class);
    } catch (JsonProcessingException e) {
      throw new UnparsableEntityException(e.getMessage());
    }
  }

}
