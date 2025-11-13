package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationApiClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationSearchClient organizationSearchClient;
  private final OrganizationEntityClient organizationEntityClient;
  private final OrganizationClient organizationClient;
  private final OrganizationApiClient organizationApiClient;

  public OrganizationServiceImpl(OrganizationSearchClient organizationSearchClient,
                                 OrganizationEntityClient organizationEntityClient,
                                 OrganizationClient organizationClient,
                                 OrganizationApiClient organizationApiClient) {
    this.organizationSearchClient = organizationSearchClient;
    this.organizationEntityClient = organizationEntityClient;
    this.organizationClient = organizationClient;
    this.organizationApiClient = organizationApiClient;
  }

  @Override
  public Organization getOrganizationByIpaCode(String ipaCode, String accessToken) {
    return organizationSearchClient.getOrganizationByIpaCode(ipaCode, accessToken);
  }

  @Override
  public PagedModelOrganization getOrganizationByBrokerIdAndOrgName(
    Long brokerId, String orgName, Pageable pageable, String accessToken) {
    return organizationSearchClient.getOrganizationByBrokerIdAndOrgName(brokerId, orgName, pageable, accessToken);
  }

  @Override
  public Organization getOrganizationByOrganizationId(Long organizationId, String accessToken) {
    return organizationEntityClient.getOrganizationByOrganizationId(organizationId, accessToken);
  }

  @Override
  public PagedModelOrganization getOrganizationsByBrokerIdAndFilters(Long brokerId, String orgName, String ipaCode, Set<Long> allowedOrganizationIds, Pageable pageable, String accessToken) {
    return organizationSearchClient.getOrganizationsByBrokerIdAndFilters(brokerId, orgName, ipaCode, allowedOrganizationIds, pageable, accessToken);
  }

  @Override
  public void updateOrganization(OrganizationDetailDTO organizationDetailDTO, String accessToken) {
    organizationClient.updateOrganization(organizationDetailDTO, accessToken);
  }

  @Override
  public OrganizationDetailDTO getOrganizationDetail(Long organizationId, String accessToken) {
    return organizationApiClient.getOrganizationDetail(organizationId, accessToken);
  }
}
