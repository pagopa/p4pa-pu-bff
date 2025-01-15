package it.gov.pagopa.pu.bff.service.taxonomy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
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
        return objectMapper.readValue(taxonomyClient.getCollectionReason(organizationType,macroAreaCode,serviceTypeCode,accessToken).toString(),CollectionModelTaxonomyCollectionReasonDTO.class);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }

  @Override
  public CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea (
    String organizationType,
    String accessToken) {
    try {
      return objectMapper.readValue(taxonomyClient.getMacroArea(organizationType,accessToken).toString(),CollectionModelTaxonomyMacroAreaCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CollectionModelTaxonomyOrganizationTypeDTO getOrganizationTypes (
    String accessToken) {
    try {
      return objectMapper.readValue(taxonomyClient.getOrganizationType(accessToken).toString(),CollectionModelTaxonomyOrganizationTypeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CollectionModelTaxonomyServiceTypeCodeDTO getServiceType (
    String organizationType,
    String macroAreaCode, String accessToken) {
    try {
      return objectMapper.readValue(taxonomyClient.getServiceType(organizationType,macroAreaCode,accessToken).toString(),CollectionModelTaxonomyServiceTypeCodeDTO.class);
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
      return objectMapper.readValue(taxonomyClient.getTaxonomyCode(organizationType,macroAreaCode,serviceTypeCode,collectionReason,accessToken).toString(),CollectionModelTaxonomyCodeDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
