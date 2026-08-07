package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.dto.generated.*;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class AuthzClient {

  private final AuthApisHolder authApisHolder;

  public AuthzClient(AuthApisHolder authApisHolder) {
    this.authApisHolder = authApisHolder;
  }

  public UserInfo getUserInfoFromMappedExternaUserId(String mappedExternalUserId, String accessToken) {
    try {
      return authApisHolder.getAuthzApi(accessToken)
        .getUserInfoFromMappedExternaUserId(mappedExternalUserId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("UserInfo with mappedExternalUserId {} not found", mappedExternalUserId);
      return null;
    }
  }

  public OperatorsPage getOrganizationOperators(String organizationIpaCode, String fiscalCode, String firstName, String lastName, Integer page, Integer size, String accessToken) {
    return authApisHolder.getAuthzApi(accessToken)
      .getOrganizationOperators(organizationIpaCode, fiscalCode, firstName, lastName, page, size);
  }

  public ClientDTOPage getClients(String organizationIpaCode, String clientId, String clientName, Pageable pageable, String accessToken) {
    return authApisHolder.getAuthzApi(accessToken)
      .getClientsSearch(organizationIpaCode, clientId, clientName, pageable.getPageNumber(), pageable.getPageSize(), PageUtils.getSortList(pageable));
  }

  public ClientDTO registerClient(String organizationIpaCode, CreateClientRequest createClientRequest, String accessToken) {
    return authApisHolder.getAuthzApi(accessToken)
      .registerClient(organizationIpaCode, createClientRequest);
  }

  public ClientDTO getClient(String organizationIpaCode, String clientId, String accessToken) {
    return authApisHolder.getAuthzApi(accessToken)
      .getClient(organizationIpaCode, clientId);
  }

  public void revokeClient(String organizationIpaCode, String clientId, String accessToken) {
    authApisHolder.getAuthzApi(accessToken).revokeClient(organizationIpaCode, clientId);
  }

  public ClientDTO generateClientSecret(String organizationIpaCode, String clientId, String accessToken) {
    try {
      return authApisHolder.getAuthzApi(accessToken)
        .generateClientSecret(organizationIpaCode, clientId);
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("CLIENT_NOT_FOUND", "Client with ID not found: " + clientId);
    }
  }

  public OperatorDTO getOrganizationOperator(String organizationIpaCode, String mappedExternalUserId, String accessToken) {
    try {
      return authApisHolder.getAuthzApi(accessToken).getOrganizationOperator(organizationIpaCode, mappedExternalUserId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Operator with mappedExternalUserId {} not found", mappedExternalUserId);
      return null;
    }
  }
}
