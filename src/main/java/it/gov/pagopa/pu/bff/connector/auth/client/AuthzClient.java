package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import lombok.extern.slf4j.Slf4j;
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
}
