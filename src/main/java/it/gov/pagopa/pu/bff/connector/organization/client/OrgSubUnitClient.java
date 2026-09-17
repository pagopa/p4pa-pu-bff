package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.OrgAndSubUnitDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrgSubUnitClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrgSubUnitClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public List<OrgAndSubUnitDTO> getOrgSubUnitWithNoServiceType(Long organizationId, PdndServiceType pdndServiceType, String accessToken) {
    return organizationApisHolder.getOrgSubUnitApi(accessToken)
      .getOrgSubUnitWithNoServiceType(organizationId, pdndServiceType);
  }
}
