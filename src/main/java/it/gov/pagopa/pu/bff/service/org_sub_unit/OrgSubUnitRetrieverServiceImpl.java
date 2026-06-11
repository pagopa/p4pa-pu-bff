package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import org.springframework.stereotype.Service;

@Service
public class OrgSubUnitRetrieverServiceImpl implements OrgSubUnitRetrieverService{

  private final OrgSubUnitService orgSubUnitService;
  private final AuthorizationService authorizationService;

  public OrgSubUnitRetrieverServiceImpl(OrgSubUnitService orgSubUnitService, AuthorizationService authorizationService) {
    this.orgSubUnitService = orgSubUnitService;
    this.authorizationService = authorizationService;
  }

  @Override
  public OrgSubUnit getOrgSubUnitById(String orgSubUnitId, String accessToken) {
    OrgSubUnit  orgSubUnit = orgSubUnitService.getOrgSubUnitById(orgSubUnitId, accessToken);
    if (orgSubUnit == null) {
      throw new ResourceNotFoundException("ORG_SUB_UNIT_NOT_FOUND", "Organization SubUnit having orgSubUnitId " + orgSubUnitId + " not found");
    }
    return orgSubUnit;
  }

  @Override
  public OrgSubUnit createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return orgSubUnitService.createOrgSubUnit(orgSubUnit, accessToken);
  }

  @Override
  public void deleteOrgSubUnit(Long organizationId, String orgSubUnitId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    orgSubUnitService.deleteOrgSubUnit(orgSubUnitId, accessToken);
  }

  @Override
  public OrgSubUnit updateOrgSubUnit(Long organizationId, String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return orgSubUnitService.updateOrgSubUnit(orgSubUnitId, orgSubUnit, accessToken);
  }
}
