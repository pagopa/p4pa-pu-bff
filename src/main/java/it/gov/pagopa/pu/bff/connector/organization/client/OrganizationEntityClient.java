package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class OrganizationEntityClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationEntityClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public Organization getOrganizationByOrganizationId(Long organizationId, String accessToken) {
    try {
      return organizationApisHolder.getOrganizationEntityControllerApi(accessToken)
        .crudGetOrganization(String.valueOf(organizationId));
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Organization with organizationId {} not found", organizationId);
      return null;
    }
  }

  public PagedModelOrganization getOrganizationByBrokerIdAndOrgName(
    String brokerId, String orgName, Pageable pageable, String accessToken) {
    return organizationApisHolder.getOrganizationSearchControllerApi(
        accessToken)
      .crudOrganizationsFindByBrokerIdAndOrgName(brokerId, orgName,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

}
