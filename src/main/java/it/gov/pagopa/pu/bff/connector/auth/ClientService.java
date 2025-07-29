package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import org.springframework.data.domain.Pageable;

public interface ClientService {
  ClientDTOPage getClients(String organizationIpaCode, String clientId, String clientName, Pageable pageable, String accessToken);
}
