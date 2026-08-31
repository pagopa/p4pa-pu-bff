package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class PdndServiceClient {
  private final OrganizationApisHolder organizationApisHolder;

  public PdndServiceClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public PdndService savePdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, String accessToken) {
    return organizationApisHolder.getPdndServiceApi(accessToken)
      .savePdndService(organizationId, pdndServiceRequestDTO, subUnitCode);
  }

  public PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, String accessToken) {
    return organizationApisHolder.getPdndServiceApi(accessToken)
      .getPdndService(organizationId, purposeId, subUnitCode);
  }
}
