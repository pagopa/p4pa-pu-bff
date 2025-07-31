package it.gov.pagopa.pu.bff.service.clients;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import org.springframework.data.domain.Pageable;

public interface ClientRetrieverService {
  ClientDTOPage getClients(Long organizationId, String clientId, String clientName, Pageable pageable, UserInfo loggedUser, String accessToken);
  ClientDTO registerClient(Long organizationId, CreateClientRequest createClientRequest, UserInfo loggedUser, String accessToken);
}
