package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.PdndServiceClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class PdndServiceServiceImpl implements PdndServiceService {

  private final PdndServiceClient pdndServiceClient;

  public PdndServiceServiceImpl(PdndServiceClient pdndServiceClient) {
    this.pdndServiceClient = pdndServiceClient;
  }

  @Override
  public PdndService savePdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, String accessToken) {
    return pdndServiceClient.savePdndService(organizationId, pdndServiceRequestDTO, subUnitCode, accessToken);
  }

  @Override
  public PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, String accessToken) {
    return pdndServiceClient.getPdndService(organizationId, purposeId, subUnitCode, accessToken);
  }
}
