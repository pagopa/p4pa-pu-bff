package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityClient;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import org.springframework.stereotype.Service;

@Service
public class OrgSubUnitServiceImpl implements OrgSubUnitService {

  private final OrgSubUnitEntityClient client;

  public OrgSubUnitServiceImpl(OrgSubUnitEntityClient client) {
    this.client = client;
  }

  @Override
  public OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken) {
    return client.getOrgSubUnitById(orgSubUnitId, accessToken);
  }

  @Override
  public OrgSubUnit createOrgSubUnit(OrgSubUnitRequestBody orgSubUnit, String accessToken) {
    return client.createOrgSubUnit(orgSubUnit, accessToken);
  }

  @Override
  public void deleteOrgSubUnit(String orgSubUnitId, String accessToken) {
    client.deleteOrgSubUnit(orgSubUnitId, accessToken);
  }

  @Override
  public OrgSubUnit updateOrgSubUnit(String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, String accessToken) {
    return client.updateOrgSubUnit(orgSubUnitId, orgSubUnit, accessToken);
  }
}
