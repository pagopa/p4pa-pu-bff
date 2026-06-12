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
  public OrgSubUnit getOrgSubUnitById(Long organizationId, String subUnitCode, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    OrgSubUnit orgSubUnit = orgSubUnitService.getOrgSubUnitById(calculateOrgSubUnitId(organizationId, subUnitCode), accessToken);
    if (orgSubUnit == null) {
      throw new ResourceNotFoundException("ORG_SUB_UNIT_NOT_FOUND", "Organization SubUnit having subUnitCode " + subUnitCode + " not found");
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
  public void deleteOrgSubUnit(Long organizationId, String subUnitCode, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    orgSubUnitService.deleteOrgSubUnit(calculateOrgSubUnitId(organizationId, subUnitCode), accessToken);
  }

  @Override
  public OrgSubUnit updateOrgSubUnit(Long organizationId, String subUnitCode, OrgSubUnitRequestBody orgSubUnit, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    validateOrganizationForSubUnit(organizationId, orgSubUnit.getOrganizationId());
    return orgSubUnitService.updateOrgSubUnit(calculateOrgSubUnitId(organizationId, subUnitCode), orgSubUnit, accessToken);
  }


  private void validateOrganizationForSubUnit(Long organizationId, Long orgIdFromSubUnit) {
    if(!organizationId.equals(orgIdFromSubUnit)){
      throw new InvalidOrgSubUnitException("INVALID_ORG_SUB_UNIT",
        String.format("Mismatch organizationId %s retrieved from path request with organizationId %s retrieved from body request", organizationId , orgIdFromSubUnit));
    }
  }

  private String calculateOrgSubUnitId(Long organizationId, String subUnitCode){
    return organizationId+"-"+subUnitCode;
  }

}
