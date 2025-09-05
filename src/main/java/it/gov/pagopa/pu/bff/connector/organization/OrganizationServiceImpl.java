package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationApiClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.pu.bff.config.CacheConfig.Fields.organization)
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationSearchClient organizationSearchClient;
  private final OrganizationEntityClient organizationEntityClient;
  private final OrganizationApiClient organizationApiClient;

  public OrganizationServiceImpl(OrganizationSearchClient organizationSearchClient,
                                 OrganizationEntityClient organizationEntityClient,
                                 OrganizationApiClient organizationApiClient) {
    this.organizationSearchClient = organizationSearchClient;
    this.organizationEntityClient = organizationEntityClient;
    this.organizationApiClient = organizationApiClient;
  }

  @Override
  @Cacheable(key = "#ipaCode", unless = "#result == null")
  public Organization getOrganizationByIpaCode(String ipaCode, String accessToken) {
    return organizationSearchClient.getOrganizationByIpaCode(ipaCode, accessToken);
  }

  @Override
  public PagedModelOrganization getOrganizationByBrokerIdAndOrgName(
    Long brokerId, String orgName, Pageable pageable, String accessToken) {
    return organizationSearchClient.getOrganizationByBrokerIdAndOrgName(brokerId, orgName, pageable, accessToken);
  }

  @Override
  @Cacheable(key = "#organizationId", unless = "#result == null")
  public Organization getOrganizationByOrganizationId(Long organizationId, String accessToken) {
    return organizationEntityClient.getOrganizationByOrganizationId(organizationId, accessToken);
  }

  @Override
  public PagedModelOrganization getOrganizationsByBrokerId(Long brokerId, Pageable pageable, String accessToken) {
    return organizationSearchClient.getOrganizationsByBrokerId(brokerId, pageable, accessToken);
  }

  @Override
  public OrganizationDetailDTO getOrganizationDetail(Long organizationId, String accessToken) {
    return organizationApiClient.getOrganizationDetail(organizationId, accessToken);
  }
}
