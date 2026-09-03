package it.gov.pagopa.pu.bff.service.org_sub_unit;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitService;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.exception.InvalidOrgSubUnitException;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedOrgSubUnitMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnit;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

@Service
public class OrgSubUnitRetrieverServiceImpl implements OrgSubUnitRetrieverService{

  private final OrgSubUnitService orgSubUnitService;
  private final AuthorizationService authorizationService;
  private final PagedOrgSubUnitMapper pagedOrgSubUnitMapper;

  public OrgSubUnitRetrieverServiceImpl(OrgSubUnitService orgSubUnitService, AuthorizationService authorizationService, PagedOrgSubUnitMapper pagedOrgSubUnitMapper) {
    this.orgSubUnitService = orgSubUnitService;
    this.authorizationService = authorizationService;
    this.pagedOrgSubUnitMapper = pagedOrgSubUnitMapper;
  }

  @Override
  public OrgSubUnit getOrgSubUnitById(Long organizationId, String subUnitCode, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    OrgSubUnit orgSubUnit = orgSubUnitService.getOrgSubUnitById(calculateOrgSubUnitId(organizationId, subUnitCode), accessToken);
    if (orgSubUnit == null) {
      throw new NotFoundException("ORG_SUB_UNIT_NOT_FOUND", "Organization SubUnit having subUnitCode " + subUnitCode + " not found");
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

  @Override
  public void updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    OrgSubUnit orgSubUnit = orgSubUnitService.getOrgSubUnitById(calculateOrgSubUnitId(organizationId, subUnitCode), accessToken);
    if (orgSubUnit == null) {
      throw new NotFoundException("ORG_SUB_UNIT_NOT_FOUND", "Organization SubUnit having subUnitCode " + subUnitCode + " not found");
    }
    orgSubUnitService.updateOrgSubUnitStatus(organizationId, subUnitCode, status, accessToken);
  }

  @Override
  public PagedOrgSubUnit getPagedOrgSubUnits(PagedOrgSubUnitFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
    Long organizationId = filters.getOrganizationId();
    String authorizedOperator = filters.getMappedExternalUserId();

    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    boolean isAdmin = AuthorizationService.isAdminRole(organizationId, loggedUser);

    String operatorExternalUserId;

    if (!isAdmin) {
      String loggedMappedExternalUserId = loggedUser.getMappedExternalUserId();
      if (authorizedOperator != null && !authorizedOperator.equals(loggedMappedExternalUserId)) {
        throw new AuthorizationDeniedException(
          "Access denied on organizationId " + organizationId +
            ": user " + loggedMappedExternalUserId +
            " is not an ADMIN and cannot request org-sub-units for operator " + authorizedOperator
        );
      }
      operatorExternalUserId = loggedMappedExternalUserId;
    } else {
      operatorExternalUserId = authorizedOperator;
    }

    PagedModelOrgSubUnit pagedModelOrgSubUnit = orgSubUnitService.findByOrganizationIdAndFilters(
      organizationId,
      operatorExternalUserId,
      filters.getSubUnitCode(),
      filters.getStatus(),
      filters.getSubUnitType(),
      pageable,
      accessToken
    );

    return pagedOrgSubUnitMapper.map(pagedModelOrgSubUnit);
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
