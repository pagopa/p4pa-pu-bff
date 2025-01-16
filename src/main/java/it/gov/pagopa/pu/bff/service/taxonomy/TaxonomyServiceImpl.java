package it.gov.pagopa.pu.bff.service.taxonomy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTOEmbedded;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyServiceImpl implements TaxonomyService{

  private final TaxonomyClient taxonomyClient;
  private final ObjectMapper objectMapper;

  public TaxonomyServiceImpl(TaxonomyClient taxonomyClient,ObjectMapper objectMapper){
    this.taxonomyClient = taxonomyClient;
    this.objectMapper = objectMapper;
  }
  @Override
  public CollectionModelTaxonomyCollectionReasonDTO getCollectionReason (
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String accessToken)
    {
      try {
        String collectionReason = objectMapper.writeValueAsString(taxonomyClient.getCollectionReason(organizationType,macroAreaCode,serviceTypeCode,accessToken).getEmbedded());
        return objectMapper.readValue(collectionReason,CollectionModelTaxonomyCollectionReasonDTO.class);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }

  @Override
  public CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea (
    String organizationType,
    String accessToken) {
    try {
      String macroArea = objectMapper.writeValueAsString(taxonomyClient.getMacroArea(organizationType,accessToken).getEmbedded());
      return objectMapper.readValue(macroArea,CollectionModelTaxonomyMacroAreaCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CollectionModelTaxonomyOrganizationTypeDTO getOrganizationTypes (
    String accessToken) {
    try {
      String orgTypes = objectMapper.writeValueAsString(taxonomyClient.getOrganizationType(accessToken).getEmbedded());
      return objectMapper.readValue(orgTypes,CollectionModelTaxonomyOrganizationTypeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CollectionModelTaxonomyServiceTypeCodeDTO getServiceType (
    String organizationType,
    String macroAreaCode, String accessToken) {
    try {
      String serviceType = objectMapper.writeValueAsString(taxonomyClient.getServiceType(organizationType,macroAreaCode,accessToken).getEmbedded());
      return objectMapper.readValue(serviceType,CollectionModelTaxonomyServiceTypeCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CollectionModelTaxonomyCodeDTO getTaxonomyCode (
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason,
    String accessToken) {
    try {
      String taxonomyCode = objectMapper.writeValueAsString(taxonomyClient.getTaxonomyCode(organizationType,macroAreaCode,serviceTypeCode,collectionReason,accessToken).getEmbedded());
      return objectMapper.readValue(taxonomyCode,CollectionModelTaxonomyCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
