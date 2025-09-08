package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSilServiceEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSilServiceSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrgSilServiceServiceImpl implements OrgSilServiceService {

  private final OrgSilServiceSearchClient orgSilServiceSearchClient;
  private final OrgSilServiceEntityClient orgSilServiceEntityClient;

  public OrgSilServiceServiceImpl(OrgSilServiceSearchClient orgSilServiceSearchClient, OrgSilServiceEntityClient orgSilServiceEntityClient) {
    this.orgSilServiceSearchClient = orgSilServiceSearchClient;
    this.orgSilServiceEntityClient = orgSilServiceEntityClient;
  }

  @Override
  public CollectionModelOrgSilService getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, String accessToken) {
    return orgSilServiceSearchClient.getOrgSilServices(organizationId, serviceType, accessToken);
  }

  @Override
  public OrgSilService getOrgSilServiceById(Long orgSilServiceId, String accessToken) {
    return orgSilServiceSearchClient.getOrgSilServiceById(orgSilServiceId, accessToken);
  }

  @Override
  public PagedModelOrgSilServiceView getOrgSilServicesByFilters(Long organizationId, String applicationName, OrgSilServiceType serviceType, Boolean flagLegacy, Pageable pageable, String accessToken) {
    return orgSilServiceSearchClient.getOrgSilServicesByFilters(organizationId, applicationName, serviceType, flagLegacy, pageable, accessToken);
  }

  @Override
  public OrgSilServiceDTO getOrgSilServiceByIdDecrypted(Long orgSilServiceId, String accessToken) {
    return orgSilServiceSearchClient.getOrgSilServiceByIdDecrypted(orgSilServiceId, accessToken);
  }

  @Override
  public OrgSilServiceDTO createOrUpdateOrgSilService(OrgSilServiceDTO orgSilServiceDTO, String accessToken) {
    return orgSilServiceSearchClient.createOrUpdateOrgSilService(orgSilServiceDTO, accessToken);
  }

  @Override
  public void deleteOrgSilService(Long orgSilServiceId, String accessToken) {
    orgSilServiceEntityClient.deleteOrgSilService(orgSilServiceId, accessToken);
  }

  @Override
  public OrgSilService getOrgSilServiceByOrganizationIdAndApplicationName(Long organizationId, String applicationName, String accessToken) {
    return orgSilServiceSearchClient.getOrgSilServiceByOrganizationIdAndApplicationName(organizationId, applicationName, accessToken);
  }
}
