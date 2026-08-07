package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class OrgSubUnitEntityClient {
  private final OrganizationApisHolder organizationApisHolder;

  public OrgSubUnitEntityClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken){
    try {
      return organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken)
        .crudGetOrgsubunit(orgSubUnitId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("SubUnit with id {} not found", orgSubUnitId);
      return null;
    }

  }

  public OrgSubUnit createOrgSubUnit(OrgSubUnitRequestBody orgSubUnit, String accessToken) {
    return organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken)
      .crudCreateOrgsubunit(orgSubUnit);
  }

  public void deleteOrgSubUnit(String orgSubUnitId, String accessToken) {
    try {
      organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken)
        .crudDeleteOrgsubunit(orgSubUnitId);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("ORG_SUB_UNIT_NOT_FOUND", "SubUnit with id " + orgSubUnitId + " not found");
    }
  }

  public OrgSubUnit updateOrgSubUnit(String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, String accessToken){
    try {
      return organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken)
        .crudUpdateOrgsubunit(orgSubUnitId, orgSubUnit);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("ORG_SUB_UNIT_NOT_FOUND", "SubUnit with id " + orgSubUnitId + " not found");
    }
  }
}
