package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.pu.bff.config.CacheConfig.Fields.taxonomy)
public class TaxonomyServiceImpl implements TaxonomyService {

  private final TaxonomyClient taxonomyClient;

  public TaxonomyServiceImpl(TaxonomyClient taxonomyClient) {
    this.taxonomyClient = taxonomyClient;
  }

  @Override
  @Cacheable(key = "#organizationType + '-' + #macroAreaCode + '-' + #serviceTypeCode", unless = "#result == null")
  public CollectionModelTaxonomyCollectionReasonDTO getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode, String accessToken) {
    return taxonomyClient.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken);
  }

  @Override
  @Cacheable(key = "#organizationType", unless = "#result == null")
  public CollectionModelTaxonomyMacroAreaCodeDTO getMacroArea(String organizationType, String accessToken) {
    return taxonomyClient.getMacroArea(organizationType, accessToken);
  }

  @Override
  @Cacheable(key = "'organizationTypes'", unless = "#result == null")
  public CollectionModelTaxonomyOrganizationTypeDTO getOrganizationType(String accessToken) {
    return taxonomyClient.getOrganizationType(accessToken);
  }

  @Override
  @Cacheable(key = "#organizationType + '-' + #macroAreaCode", unless = "#result == null")
  public CollectionModelTaxonomyServiceTypeCodeDTO getServiceType(String organizationType, String macroAreaCode, String accessToken) {
    return taxonomyClient.getServiceType(organizationType, macroAreaCode, accessToken);
  }

  @Override
  @Cacheable(key = "#organizationType + '-' + #macroAreaCode + '-' + #serviceTypeCode + '-' + #collectionReason", unless = "#result == null")
  public CollectionModelTaxonomyCodeDTO getTaxonomyCode(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, String accessToken) {
    return taxonomyClient.getTaxonomyCode(organizationType, macroAreaCode, serviceTypeCode, collectionReason, accessToken);
  }
}
