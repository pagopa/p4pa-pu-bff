package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrgSubUnitEntityExtendedClient {
  private final OrganizationApisHolder organizationApisHolder;

  public OrgSubUnitEntityExtendedClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void updateStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status, String accessToken) {
    organizationApisHolder.getOrgSubUnitEntityExtendedControllerApi(accessToken).updateStatus(organizationId, subUnitCode, status);
  }
}
