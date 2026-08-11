package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void updateOrganization(OrganizationDetailDTO organizationDetailDTO, String accessToken) {
    try {
      organizationApisHolder.getOrganizationApi(accessToken)
        .updateOrganization(organizationDetailDTO);
    } catch (RestInvokeNotFoundException e) {
      throw new NotFoundException("ORGANIZATION_NOT_FOUND", "Organization with organizationId " + organizationDetailDTO.getOrganizationId() + " not found");
    }
  }
}
