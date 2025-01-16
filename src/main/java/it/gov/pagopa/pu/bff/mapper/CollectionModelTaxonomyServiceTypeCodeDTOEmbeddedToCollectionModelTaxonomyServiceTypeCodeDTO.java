package it.gov.pagopa.pu.bff.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.bff.exception.UnparsableEntityException;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyServiceTypeCodeDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CollectionModelTaxonomyServiceTypeCodeDTOEmbeddedToCollectionModelTaxonomyServiceTypeCodeDTO {

  private final ObjectMapper objectMapper;

  public CollectionModelTaxonomyServiceTypeCodeDTOEmbeddedToCollectionModelTaxonomyServiceTypeCodeDTO(ObjectMapper objectMapper){
    this.objectMapper = objectMapper;
  }

  public CollectionModelTaxonomyServiceTypeCodeDTO map(
    CollectionModelTaxonomyServiceTypeCodeDTOEmbedded input){
    try{
      List<TaxonomyServiceTypeCodeDTO> modifiedList = input.getTaxonomyServiceTypeCodeDTOes().stream()
        .map(dto -> {
          dto.setOrganizationTypeDescription(dto.getOrganizationType() + ". " + dto.getOrganizationTypeDescription());
          dto.setMacroAreaDescription(dto.getMacroAreaCode()+". "+dto.getMacroAreaName());
          dto.setServiceTypeDescription(dto.getServiceTypeCode()+". "+dto.getServiceType());
          return dto;
        }).toList();
      CollectionModelTaxonomyServiceTypeCodeDTOEmbedded modifiedEmbedded = new CollectionModelTaxonomyServiceTypeCodeDTOEmbedded();
      modifiedEmbedded.setTaxonomyServiceTypeCodeDTOes(modifiedList);
      String result = objectMapper.writeValueAsString(modifiedEmbedded);
      return objectMapper.readValue(result, CollectionModelTaxonomyServiceTypeCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new UnparsableEntityException(e.getMessage());
    }
  }

}
