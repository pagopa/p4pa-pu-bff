package it.gov.pagopa.pu.bff.service.clients;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.ClientService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientRetrieverServiceImpl implements ClientRetrieverService {

  private final ClientService clientService;
  private final AuthorizationService authorizationService;
  private final OrganizationService organizationService;

  public ClientRetrieverServiceImpl(ClientService clientService,
    AuthorizationService authorizationService,
    OrganizationService organizationService) {
    this.clientService = clientService;
    this.authorizationService = authorizationService;
    this.organizationService = organizationService;
  }

  @Override
  public ClientDTOPage getClients(Long organizationId, String clientId, String clientName,
    Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    return clientService.getClients(organization.getIpaCode(), clientId, clientName, pageable, accessToken);
  }

  @Override
  public ClientDTO registerClient(Long organizationId, CreateClientRequest createClientRequest,
    UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    return clientService.registerClient(organization.getIpaCode(), createClientRequest, accessToken);
  }

  @Override
  public ClientDTO getClient(Long organizationId, String clientId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    return clientService.getClient(organization.getIpaCode(), clientId, accessToken);
  }
}
