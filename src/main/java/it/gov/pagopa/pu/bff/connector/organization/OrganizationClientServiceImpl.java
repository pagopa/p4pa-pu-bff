package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.pu.bff.config.CacheConfig.Fields.organization)
public class OrganizationClientServiceImpl implements OrganizationClientService{

  private final OrganizationSearchClient organizationSearchClient;

  public OrganizationClientServiceImpl(OrganizationSearchClient organizationSearchClient) {
    this.organizationSearchClient = organizationSearchClient;
  }

  @Override
  @Cacheable(key = "#ipaCode", unless="#result == null")
  public Organization getOrganizationByIpaCode(String ipaCode, String accessToken){
    return organizationSearchClient.getOrganizationByIpaCode(ipaCode, accessToken);
  }
}
