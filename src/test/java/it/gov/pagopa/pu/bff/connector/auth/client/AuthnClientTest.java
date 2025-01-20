package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.p4paauth.controller.generated.AuthnApi;
import it.gov.pagopa.pu.p4paauth.dto.generated.AccessToken;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  void whenPostTokenThrowsHttpClientErrorExceptionThenLogAndRethrow() {
    String clientId = "clientId";
    String grantType = "grantType";
    String scope = "scope";
    String subjectToken = "subjectToken";
    String subjectIssuer = "subjectIssuer";
    String subjectTokenType = "subjectTokenType";
    String clientSecret = "clientSecret";

    HttpClientErrorException exception = mock(HttpClientErrorException.class);
    when(authApisHolderMock.getAuthnApi(null)).thenReturn(authnApiMock);
    when(authnApiMock.postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret))
      .thenThrow(exception);

    HttpClientErrorException thrown = assertThrows(
      HttpClientErrorException.class,
      () -> authnClient.postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret)
    );

    assertSame(exception, thrown);
    verify(authApisHolderMock).getAuthnApi(null);
    verify(authnApiMock).postToken(clientId, grantType, scope, subjectToken, subjectIssuer, subjectTokenType, clientSecret);
  }

}
