package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityExtendedClient;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import org.springframework.stereotype.Service;

@Service
public class OrgSubUnitServiceImpl implements OrgSubUnitService {
  private final OrgSubUnitEntityClient orgSubUnitEntityClient;
  private final OrgSubUnitEntityExtendedClient orgSubUnitEntityExtendedClient;

  public OrgSubUnitServiceImpl(OrgSubUnitEntityClient orgSubUnitEntityClient, OrgSubUnitEntityExtendedClient orgSubUnitEntityExtendedClient) {
    this.orgSubUnitEntityClient = orgSubUnitEntityClient;
    this.orgSubUnitEntityExtendedClient = orgSubUnitEntityExtendedClient;
  }

  @Override
  public OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken) {
    return orgSubUnitEntityClient.getOrgSubUnitById(orgSubUnitId, accessToken);
  }

  @Override
  public OrgSubUnit createOrgSubUnit(OrgSubUnitRequestBody orgSubUnit, String accessToken) {
    return orgSubUnitEntityClient.createOrgSubUnit(orgSubUnit, accessToken);
  }

  @Override
  public void deleteOrgSubUnit(String orgSubUnitId, String accessToken) {
    orgSubUnitEntityClient.deleteOrgSubUnit(orgSubUnitId, accessToken);
  }

  @Override
  public OrgSubUnit updateOrgSubUnit(String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, String accessToken) {
    return orgSubUnitEntityClient.updateOrgSubUnit(orgSubUnitId, orgSubUnit, accessToken);
  }

  @Override
  public void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, String accessToken) {
    orgSubUnitEntityExtendedClient.updateStatus(organizationId, subUnitCode, status, accessToken);
  }
}
