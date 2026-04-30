package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

  private final AuthzClient authzClient;

  public ClientServiceImpl(AuthzClient authzClient) {
    this.authzClient = authzClient;
  }

  @Override
  public ClientDTOPage getClients(String organizationIpaCode, String clientId, String clientName,
    Pageable pageable, String accessToken) {
    return authzClient.getClients(organizationIpaCode, clientId, clientName, pageable, accessToken);
  }

  @Override
  public ClientDTO registerClient(String organizationIpaCode, CreateClientRequest createClientRequest, String accessToken) {
    return authzClient.registerClient(organizationIpaCode, createClientRequest, accessToken);
  }

  @Override
  public ClientDTO getClient(String organizationIpaCode, String clientId, String accessToken) {
    return authzClient.getClient(organizationIpaCode, clientId, accessToken);
  }

  @Override
  public void deleteClient(String organizationIpaCode, String clientId, String accessToken) {
    authzClient.revokeClient(organizationIpaCode, clientId, accessToken);
  }

  @Override
  public ClientDTO generateClientSecret(String organizationIpaCode, String clientId, String accessToken) {
    return authzClient.generateClientSecret(organizationIpaCode, clientId, accessToken);
  }
}
