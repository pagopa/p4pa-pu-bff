package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import org.springframework.data.domain.Pageable;

public interface ClientService {
  ClientDTOPage getClients(String organizationIpaCode, String clientId, String clientName, Pageable pageable, String accessToken);
  ClientDTO registerClient(String organizationIpaCode, CreateClientRequest createClientRequest, String accessToken);
}
