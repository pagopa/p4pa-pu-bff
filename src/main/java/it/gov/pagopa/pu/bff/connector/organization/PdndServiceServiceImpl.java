package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.PdndServiceClient;
import it.gov.pagopa.pu.bff.connector.organization.client.PdndServiceSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdndServiceServiceImpl implements PdndServiceService {

  private final PdndServiceClient pdndServiceClient;
  private final PdndServiceSearchClient pdndServiceSearchClient;

  public PdndServiceServiceImpl(PdndServiceClient pdndServiceClient, PdndServiceSearchClient pdndServiceSearchClient) {
    this.pdndServiceClient = pdndServiceClient;
    this.pdndServiceSearchClient = pdndServiceSearchClient;
  }

  @Override
  public PdndService savePdndService(Long organizationId, PdndServiceRequestDTO pdndServiceRequestDTO, String subUnitCode, String accessToken) {
    return pdndServiceClient.savePdndService(organizationId, pdndServiceRequestDTO, subUnitCode, accessToken);
  }

  @Override
  public PdndServiceDTO getPdndService(Long organizationId, String purposeId, String subUnitCode, String accessToken) {
    return pdndServiceClient.getPdndService(organizationId, purposeId, subUnitCode, accessToken);
  }

  @Override
  public List<PdndServiceDTO> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType pdndServiceType, String accessToken) {
    return pdndServiceClient.getPdndServices(organizationId, subUnitCode, pdndServiceType, accessToken);
  }

  @Override
  public List<PdndService> findByOrganizationIdAndClientId(Long organizationId, String clientId, PdndServiceType serviceType, String accessToken) {
    return pdndServiceSearchClient.findByOrganizationIdAndClientId(organizationId, clientId, serviceType, accessToken);
  }
}
