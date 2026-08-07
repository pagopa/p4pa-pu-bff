package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class OrgSilServiceEntityClient {
  private final OrganizationApisHolder organizationApisHolder;

  public OrgSilServiceEntityClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void deleteOrgSilService(Long orgSilServiceId, String accessToken) {
    try {
      organizationApisHolder.getOrgSilServiceEntityControllerApi(accessToken)
        .crudDeleteOrgsilservice(String.valueOf(orgSilServiceId));
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("ORG_SIL_SERVICE_NOT_FOUND", "OrgSilService with ID %d not found".formatted(orgSilServiceId));
    }
  }
}
