package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.controller.generated.AuthnApi;
import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.LimitedTokenRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthnClientTest {

  @Mock
  private AuthApisHolder authApisHolderMock;
  @Mock
  private AuthnApi authnApiMock;

  private AuthnClient authnClient;

  @BeforeEach
  void setUp() {
    authnClient = new AuthnClient(authApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authApisHolderMock
    );
  }

  @Test
  void whenGetUserInfoThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    UserInfo expectedResult = new UserInfo();

    when(authApisHolderMock.getAuthnApi(accessToken))
      .thenReturn(authnApiMock);
    when(authnApiMock.getUserInfo())
      .thenReturn(expectedResult);

    UserInfo result = authnClient.getUserInfo(accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenUnauthorizedExceptionWhenGetUserInfoThenThrowInvalidAccessTokenException() {
    String accessToken = "ACCESSTOKEN";

    when(authApisHolderMock.getAuthnApi(accessToken))
      .thenReturn(authnApiMock);
    when(authnApiMock.getUserInfo())
      .thenThrow(HttpClientErrorException.create(
        HttpStatus.UNAUTHORIZED,
        "Unauthorized",
        null,
        "bodyMessage".getBytes(),
        null
      ));

    InvalidAccessTokenException exception = Assertions.assertThrows(
      InvalidAccessTokenException.class,
      () -> authnClient.getUserInfo(accessToken)
    );

    assertEquals("INVALID_ACCESS_TOKEN", exception.getCode());
    assertEquals("The provided access token is invalid or expired", exception.getMessage());
  }

  @Test
  void whenPostTokenThenInvokeAuthnApi() {
    String clientId = "clientId";
    String grantType = "grantType";
    String scope = "scope";
    String subjectToken = "subjectToken";
    String subjectIssuer = "subjectIssuer";
    String subjectTokenType = "subjectTokenType";
    String clientSecret = "clientSecret";

    AccessToken expectedToken = new AccessToken();
    expectedToken.setAccessToken("mockAccessToken");
    expectedToken.setTokenType("Bearer");
    expectedToken.setExpiresIn(3600);

    when(authApisHolderMock.getAuthnApi(null)).thenReturn(authnApiMock);
    when(authnApiMock.postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret))
      .thenReturn(expectedToken);

    AccessToken result = authnClient.postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret);

    assertSame(expectedToken, result);
    verify(authApisHolderMock).getAuthnApi(null);
    verify(authnApiMock).postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret);
  }

  @Test
  void whenLogoutThenInvokeAuthnApi() {
    String clientId = "clientId";
    String accessToken = "accessToken";

    when(authApisHolderMock.getAuthnApi(null)).thenReturn(authnApiMock);
    doNothing().when(authnApiMock).logout(clientId, accessToken);

    authnClient.logout(clientId, accessToken);

    Mockito.verifyNoMoreInteractions(authnApiMock);
  }

  @Test
  void whenPostLimitedTokenThenInvokeAuthnApi() {
    String accessToken = "accessToken";
    LimitedTokenRequest limitedTokenRequest = new LimitedTokenRequest();
    limitedTokenRequest.app("APP");
    limitedTokenRequest.resource("RESOURCES");
    limitedTokenRequest.organizationId(1L);

    AccessToken expectedToken = new AccessToken();
    expectedToken.setAccessToken("mockAccessToken");
    expectedToken.setTokenType("Bearer");
    expectedToken.setExpiresIn(3600);

    when(authApisHolderMock.getAuthnApi(accessToken)).thenReturn(authnApiMock);
    when(authnApiMock.postLimitedToken(limitedTokenRequest)).thenReturn(expectedToken);

    AccessToken result = authnClient.postLimitedToken(limitedTokenRequest, accessToken);

    assertSame(expectedToken, result);
  }
}
