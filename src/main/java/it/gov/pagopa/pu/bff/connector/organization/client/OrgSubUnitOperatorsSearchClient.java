package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrgSubUnitOperatorsSearchClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrgSubUnitOperatorsSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PagedModelOrgSubUnitOperators findByOrganizationIdAndSubUnitCode(Long organizationId, String subUnitCode, Pageable pageable, String accessToken) {
    return organizationApisHolder.getOrgSubUnitOperatorsSearchControllerApi(accessToken).crudOrgSubUnitOperatorsFindByOrganizationIdAndSubUnitCode(
      organizationId,
      subUnitCode,
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)
    );
  }
}
