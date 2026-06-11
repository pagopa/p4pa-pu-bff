package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitService;
import it.gov.pagopa.pu.bff.exception.InvalidOrgSubUnitException;
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
  public OrgSubUnit getOrgSubUnitById(Long organizationId, String orgSubUnitId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    validateOrganizationForSubUnit(organizationId, orgSubUnitId);
    OrgSubUnit  orgSubUnit = orgSubUnitService.getOrgSubUnitById(orgSubUnitId, accessToken);
    if (orgSubUnit == null) {
      throw new ResourceNotFoundException("ORG_SUB_UNIT_NOT_FOUND", "Organization SubUnit having orgSubUnitId " + orgSubUnitId + " not found");
    }
    return orgSubUnit;
  }

  @Override
  public OrgSubUnit createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    validateOrganizationForSubUnit(organizationId, orgSubUnit.getOrganizationId());
    return orgSubUnitService.createOrgSubUnit(orgSubUnit, accessToken);
  }

  @Override
  public void deleteOrgSubUnit(Long organizationId, String orgSubUnitId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    validateOrganizationForSubUnit(organizationId, orgSubUnitId);
    orgSubUnitService.deleteOrgSubUnit(orgSubUnitId, accessToken);
  }

  @Override
  public OrgSubUnit updateOrgSubUnit(Long organizationId, String orgSubUnitId, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    validateOrganizationForSubUnit(organizationId, orgSubUnitId);
    validateOrganizationForSubUnit(organizationId, orgSubUnit.getOrganizationId());
    return orgSubUnitService.updateOrgSubUnit(orgSubUnitId, orgSubUnit, accessToken);
  }

  private void validateOrganizationForSubUnit(Long organizationId, String orgSubUnitId) {
    try {
      Long orgIdFromSubUnit = Long.valueOf(orgSubUnitId.split("-")[0]);
      validateOrganizationForSubUnit(organizationId, orgIdFromSubUnit);
    } catch (NumberFormatException e) {
      throw new InvalidOrgSubUnitException("INVALID_ORG_SUB_UNIT", "Error while retrieve organizationId from orgSubUnitId: "+orgSubUnitId);
    }
  }

  private void validateOrganizationForSubUnit(Long organizationId, Long orgIdFromSubUnit) {
    if(!organizationId.equals(orgIdFromSubUnit)){
      throw new InvalidOrgSubUnitException("INVALID_ORG_SUB_UNIT",
        String.format("Mismatch organizationId %s retrieved from request with organizationId %s retrieved from OrgSubUnitId", organizationId , orgIdFromSubUnit));
    }
  }

}
