package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgSubUnitOperatorsClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrgSubUnitOperatorsClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, String accessToken) {
    organizationApisHolder.getOrgSubUnitOperatorsApi(accessToken)
      .addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes);
  }
}
