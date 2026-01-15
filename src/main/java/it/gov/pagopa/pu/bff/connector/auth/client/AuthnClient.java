package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.LimitedTokenRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
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
    try {
      return authApisHolder.getAuthnApi(accessToken)
        .getUserInfo();
    } catch (HttpClientErrorException.Unauthorized e) {
      throw new InvalidAccessTokenException("INVALID_ACCESS_TOKEN", "The provided access token is invalid or expired");
    }

  }

  public AccessToken postToken(String clientId, String grantType, String scope, String subjectToken, String subjectIssuer, String subjectTokenType, String clientSecret) {
    return authApisHolder.getAuthnApi(null)
      .postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret);
  }

  public AccessToken postLimitedToken(LimitedTokenRequest limitedTokenRequest, String accessToken) {
    return authApisHolder.getAuthnApi(accessToken).postLimitedToken(limitedTokenRequest);
  }

  public void logout(String clientId, String accessToken) {
    authApisHolder.getAuthnApi(null)
      .logout(clientId, accessToken);
  }
}
