package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnit;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrgSubUnitSearchClient {
  private final OrganizationApisHolder organizationApisHolder;

  public OrgSubUnitSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PagedModelOrgSubUnit findByOrganizationIdAndFilters(
    Long organizationId,
    String operatorExternalUserId,
    PagedOrgSubUnitFiltersDTO filters,
    Pageable pageable,
    String accessToken
  ) {
    return organizationApisHolder.getOrgSubUnitSearchControllerApi(accessToken)
      .crudOrgSubUnitFindByOrganizationIdAndFilters(
        organizationId,
        operatorExternalUserId,
        filters.getSubUnitCode(),
        filters.getSubUnitName(),
        filters.getStatus(),
        filters.getSubUnitType(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable)
      );
  }

  public PagedModelOrgSubUnit findOperatorAssignableOrgSubUnits(
    Long organizationId,
    String operatorExternalUserId,
    String subUnitCode,
    String subUnitName,
    Pageable pageable,
    String accessToken
  ) {
    return organizationApisHolder.getOrgSubUnitSearchControllerApi(accessToken)
      .crudOrgSubUnitFindOrgSubUnitsAssignableToOperator(
        organizationId,
        operatorExternalUserId,
        subUnitCode,
        subUnitName,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable)
      );
  }
}
