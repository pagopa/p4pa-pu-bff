package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSilServiceSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
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
}
