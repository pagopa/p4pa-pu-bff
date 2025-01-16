package it.gov.pagopa.pu.bff.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.exception.UnparsableEntityException;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyMacroAreaCodeDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CollectionModelTaxonomyMacroAreaCodeDTOEmbeddedToCollectionModelTaxonomyMacroAreaCodeDTO {
  private final ObjectMapper objectMapper;

  public CollectionModelTaxonomyMacroAreaCodeDTOEmbeddedToCollectionModelTaxonomyMacroAreaCodeDTO(ObjectMapper objectMapper){
    this.objectMapper = objectMapper;
  }

  public CollectionModelTaxonomyMacroAreaCodeDTO map(
    CollectionModelTaxonomyMacroAreaCodeDTOEmbedded input){
    try{
      List<TaxonomyMacroAreaCodeDTO> modifiedList = input.getTaxonomyMacroAreaCodeDTOes().stream()
        .map(dto -> {
          dto.setOrganizationTypeDescription(dto.getOrganizationType() + ". " + dto.getOrganizationTypeDescription());
          dto.setMacroAreaDescription(dto.getMacroAreaCode()+". "+dto.getMacroAreaName());
          return dto;
        }).toList();
      CollectionModelTaxonomyMacroAreaCodeDTOEmbedded modifiedEmbedded = new CollectionModelTaxonomyMacroAreaCodeDTOEmbedded();
      modifiedEmbedded.setTaxonomyMacroAreaCodeDTOes(modifiedList);
      String macroAreaCode = objectMapper.writeValueAsString(modifiedEmbedded);
      return objectMapper.readValue(macroAreaCode, CollectionModelTaxonomyMacroAreaCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new UnparsableEntityException(e.getMessage());
    }
  }
}
