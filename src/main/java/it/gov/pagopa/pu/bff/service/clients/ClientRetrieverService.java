package it.gov.pagopa.pu.bff.service.clients;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import org.springframework.data.domain.Pageable;

public interface ClientRetrieverService {
  ClientDTOPage getClients(Long organizationId, String clientId, String clientName, Pageable pageable, UserInfo loggedUser, String accessToken);
}
