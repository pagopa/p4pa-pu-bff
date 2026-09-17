package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void updateOrganization(OrganizationUpdateDTO organizationUpdateDTO, String accessToken) {
    try {
      organizationApisHolder.getOrganizationApi(accessToken)
        .updateOrganization(organizationUpdateDTO);
    } catch (RestInvokeNotFoundException e) {
      throw new NotFoundException("ORGANIZATION_NOT_FOUND", "Organization with organizationId " + organizationUpdateDTO.getOrganizationId() + " not found");
    }
  }
}
