package it.gov.pagopa.pu.bff.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.exception.UnparsableEntityException;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyOrganizationTypeDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CollectionModelTaxonomyOrganizationTypeDTOEmbeddedToCollectionModelTaxonomyOrganizationTypeDTO {

  private final ObjectMapper objectMapper;

  public CollectionModelTaxonomyOrganizationTypeDTOEmbeddedToCollectionModelTaxonomyOrganizationTypeDTO(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public CollectionModelTaxonomyOrganizationTypeDTO map(
    CollectionModelTaxonomyOrganizationTypeDTOEmbedded input) {
    try {
      // Modify the list of DTOs
      List<TaxonomyOrganizationTypeDTO> modifiedList = input.getTaxonomyOrganizationTypeDTOes().stream()
        .map(dto -> {
          dto.setOrganizationTypeDescription(dto.getOrganizationType() + ". " + dto.getOrganizationTypeDescription());
          return dto;
        })
        .toList();

      // Create a new embedded object with the modified list
      CollectionModelTaxonomyOrganizationTypeDTOEmbedded modifiedEmbedded = new CollectionModelTaxonomyOrganizationTypeDTOEmbedded();
      modifiedEmbedded.setTaxonomyOrganizationTypeDTOes(modifiedList);

      // Convert the modified embedded object to JSON and back to CollectionModel
      String orgTypesJson = objectMapper.writeValueAsString(modifiedEmbedded);
      return objectMapper.readValue(orgTypesJson, CollectionModelTaxonomyOrganizationTypeDTO.class);
    } catch (JsonProcessingException e) {
      throw new UnparsableEntityException(e.getMessage());
    }
  }

}
