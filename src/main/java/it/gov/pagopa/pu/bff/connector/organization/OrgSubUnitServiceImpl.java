package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitEntityExtendedClient;
import it.gov.pagopa.pu.bff.connector.organization.client.OrgSubUnitSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgSubUnitServiceImpl implements OrgSubUnitService {
  private final OrgSubUnitEntityClient orgSubUnitEntityClient;
  private final OrgSubUnitEntityExtendedClient orgSubUnitEntityExtendedClient;
  private final OrgSubUnitSearchClient orgSubUnitSearchClient;
  private final OrgSubUnitClient orgSubUnitClient;

  public OrgSubUnitServiceImpl(
    OrgSubUnitEntityClient orgSubUnitEntityClient,
    OrgSubUnitEntityExtendedClient orgSubUnitEntityExtendedClient,
    OrgSubUnitSearchClient orgSubUnitSearchClient, OrgSubUnitClient orgSubUnitClient
  ) {
    this.orgSubUnitEntityClient = orgSubUnitEntityClient;
    this.orgSubUnitEntityExtendedClient = orgSubUnitEntityExtendedClient;
    this.orgSubUnitSearchClient = orgSubUnitSearchClient;
    this.orgSubUnitClient = orgSubUnitClient;
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

  @Override
  public PagedModelOrgSubUnit findByOrganizationIdAndFilters(
    Long organizationId,
    String operatorExternalUserId,
    String subUnitCode,
    OrgSubUnitStatus status,
    SubUnitType subUnitType,
    Pageable pageable,
    String accessToken
  ) {
     return orgSubUnitSearchClient.findByOrganizationIdAndFilters(
      organizationId,
      operatorExternalUserId,
      subUnitCode,
      status,
      subUnitType,
      pageable,
      accessToken
    );
  }

  @Override
  public List<OrgAndSubUnitDTO> getOrgSubUnitWithNoServiceType(Long organizationId, PdndServiceType pdndServiceType, String accessToken) {
    return orgSubUnitClient.getOrgSubUnitWithNoServiceType(organizationId, pdndServiceType, accessToken);
  }
}
