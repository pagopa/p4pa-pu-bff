package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrgSilServiceSearchClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrgSilServiceSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public CollectionModelOrgSilService getOrgSilServices(Long organizationId, OrgSilServiceType serviceType, String accessToken) {
    return organizationApisHolder.getOrgSilServiceSearchControllerApi(accessToken)
      .crudOrgSilServicesFindAllByOrganizationIdAndServiceType(organizationId,serviceType);
  }
}
