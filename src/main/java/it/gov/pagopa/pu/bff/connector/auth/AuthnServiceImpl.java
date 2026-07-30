package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.LimitedTokenRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.dto.PostTokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthnServiceImpl implements AuthnService {
  public static final String CLIENT_ID = "piattaforma-unitaria";
  public static final String GRANT_TYPE_TOKEN_EXCHANGE = "urn:ietf:params:oauth:grant-type:token-exchange";
  public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
  public static final String SCOPE = "openid";
  public static final String SUBJECT_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:jwt";
  private final String subjectIssuer;

  private final AuthnClient client;

  public AuthnServiceImpl(AuthnClient client, @Value("${rest.auth.token-exchange-issuer}") String subjectIssuer) {
    this.client = client;
    this.subjectIssuer = subjectIssuer;
  }

  @Override
  public UserInfo getUserInfo(String accessToken) {
    return client.getUserInfo(accessToken);
  }

  @Override
  public AccessToken postToken(String subjectToken) {
    return client.postToken(
      PostTokenRequest.builder()
        .clientId(CLIENT_ID)
        .grantType(GRANT_TYPE_TOKEN_EXCHANGE)
        .scope(SCOPE)
        .subjectToken(subjectToken)
        .subjectIssuer(subjectIssuer)
        .subjectTokenType(SUBJECT_TOKEN_TYPE)
        .build());
  }

  @Override
  public AccessToken postLimitedToken(LimitedTokenRequest limitedTokenRequest, String accessToken) {
    return client.postLimitedToken(limitedTokenRequest, accessToken);
  }

  @Override
  public void logout(String accessToken) {
    client.logout(CLIENT_ID, accessToken);
  }

  @Override
  public AccessToken refreshToken(String refreshToken) {
    return client.postToken(
      PostTokenRequest.builder()
        .clientId(CLIENT_ID)
        .grantType(GRANT_TYPE_REFRESH_TOKEN)
        .scope(SCOPE)
        .refreshToken(refreshToken)
        .build());
  }
}
