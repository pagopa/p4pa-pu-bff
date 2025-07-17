package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSilServiceSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSilServiceView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrgSilServiceServiceImpl implements OrgSilServiceService {

  private final OrgSilServiceSearchClient orgSilServiceSearchClient;

  public OrgSilServiceServiceImpl(OrgSilServiceSearchClient orgSilServiceSearchClient) {
      this.orgSilServiceSearchClient = orgSilServiceSearchClient;
  }

  @Override
  public CollectionModelOrgSilService getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, String accessToken) {
    return orgSilServiceSearchClient.getOrgSilServices(organizationId,serviceType,accessToken);
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
}
