package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
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
}
