package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.p4paauth.dto.generated.AccessToken;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class AuthnClient {

  private final AuthApisHolder authApisHolder;

  public AuthnClient(AuthApisHolder authApisHolder) {
    this.authApisHolder = authApisHolder;
  }

  public UserInfo getUserInfo(String accessToken) {
    return authApisHolder.getAuthnApi(accessToken)
      .getUserInfo();
  }

  public AccessToken postToken(String clientId, String grantType, String scope, String subjectToken, String subjectIssuer, String subjectTokenType, String clientSecret) {
    try {
      return authApisHolder.getAuthnApi(null)
        .postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret);

    } catch (HttpClientErrorException e) {
      log.error("Error during token exchange: {}", e.getStatusCode(), e);
      throw e;
    }
  }

}
