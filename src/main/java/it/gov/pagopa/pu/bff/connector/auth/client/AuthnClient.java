package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.LimitedTokenRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.bff.dto.PostTokenRequest;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    } catch (RestInvokeNotAuthorizedException e) {
      throw new InvalidAccessTokenException("INVALID_ACCESS_TOKEN", e.getMessage());
    }
  }

  public AccessToken postToken(PostTokenRequest postTokenRequest) {
    return authApisHolder.getAuthnApi(null)
      .postToken(
        postTokenRequest.getClientId(),
        postTokenRequest.getGrantType(),
        postTokenRequest.getScope(),
        postTokenRequest.getSubjectToken(),
        postTokenRequest.getSubjectIssuer(),
        postTokenRequest.getSubjectTokenType(),
        null,
        postTokenRequest.getRefreshToken()
      );
  }

  public AccessToken postLimitedToken(LimitedTokenRequest limitedTokenRequest, String accessToken) {
    return authApisHolder.getAuthnApi(accessToken).postLimitedToken(limitedTokenRequest);
  }

  public void logout(String clientId, String accessToken) {
    authApisHolder.getAuthnApi(null)
      .logout(clientId, accessToken);
  }
}
