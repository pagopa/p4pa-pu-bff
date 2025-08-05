package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import it.gov.pagopa.pu.bff.controller.generated.ClientsApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.clients.ClientRetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ClientController implements ClientsApi {
  private final ClientRetrieverService clientRetrieverService;

  public ClientController(ClientRetrieverService clientRetrieverService) {
    this.clientRetrieverService = clientRetrieverService;
  }

  @Override
  public ResponseEntity<ClientDTOPage> getClients(Long organizationId, String clientId, String clientName, Pageable pageable) {
    log.info("User requested getClients having organizationId {}, clientId {} and clientName {}", organizationId, clientId, clientName);
    return ResponseEntity.ok(clientRetrieverService.getClients(organizationId, clientId, clientName, pageable,
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<ClientDTO> registerClient(Long organizationId,
    CreateClientRequest createClientRequest) {
    log.info("User requested for register new client for organizationId {} having clientName {}", organizationId, createClientRequest.getClientName());
    return ResponseEntity.ok(clientRetrieverService.registerClient(organizationId,createClientRequest,
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<ClientDTO> getClient(Long organizationId, String clientId) {
    log.info("User requested getClient having organizationId {} and clientId {}", organizationId, clientId);
    return ResponseEntity.ok(clientRetrieverService.getClient(
      organizationId, clientId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deleteClient(Long organizationId, String clientId) {
    log.info("User requested delete client for organizationId {} and clientId {}", organizationId, clientId);
    clientRetrieverService.deleteClient(organizationId, clientId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<ClientDTO> generateClientSecret(Long organizationId, String clientId) {
    log.info("User requested generateClientSecret for client {} having organizationId {}", clientId, organizationId);
    return ResponseEntity.ok(clientRetrieverService.generateClientSecret(
      organizationId, clientId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
