package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class OrganizationApiClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationApiClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public OrganizationDetailDTO getOrganizationDetail(Long organizationId, String accessToken) {
    try {
      return organizationApisHolder.getOrganizationApi(accessToken)
        .getOrganization(organizationId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Organization with organizationId {} not found", organizationId);
      return null;
    }
  }
}
