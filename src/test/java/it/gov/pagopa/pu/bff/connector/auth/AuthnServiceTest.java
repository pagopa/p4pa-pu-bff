package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.LimitedTokenRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.dto.PostTokenRequest;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static it.gov.pagopa.pu.bff.connector.auth.AuthnServiceImpl.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthnServiceTest {
  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private AuthnClient clientMock;
  private final String subjectIssuer = "subjectIssuer";

  private AuthnService service;

  @BeforeEach
  void setUp() {
    service = new AuthnServiceImpl(clientMock, subjectIssuer);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(clientMock);
  }

  @Test
  void whenGetUserInfoThenInvokeClient() {
    String accessToken = "accessToken";
    UserInfo expectedResult = podamFactory.manufacturePojo(UserInfo.class);

    when(clientMock.getUserInfo(accessToken)).thenReturn(expectedResult);

    UserInfo result = service.getUserInfo(accessToken);

    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenPostTokenThenInvokeClient() {
    String subjectToken = "subjectToken";

    PostTokenRequest postTokenRequest = PostTokenRequest.builder()
      .clientId(AuthnServiceImpl.CLIENT_ID)
      .grantType(GRANT_TYPE_TOKEN_EXCHANGE)
      .scope(AuthnServiceImpl.SCOPE)
      .subjectToken(subjectToken)
      .subjectIssuer(subjectIssuer)
      .subjectTokenType(AuthnServiceImpl.SUBJECT_TOKEN_TYPE)
      .build();
    AccessToken expectedResult = podamFactory.manufacturePojo(AccessToken.class);

    when(clientMock.postToken(postTokenRequest)).thenReturn(expectedResult);

    AccessToken result = service.postToken(subjectToken);

    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenPostLimitedTokenThenInvokeClient() {
    String accessToken = "accessToken";
    LimitedTokenRequest limitedTokenRequest = podamFactory.manufacturePojo(LimitedTokenRequest.class);
    AccessToken expectedResult = podamFactory.manufacturePojo(AccessToken.class);

    when(clientMock.postLimitedToken(limitedTokenRequest,accessToken)).thenReturn(expectedResult);

    AccessToken result = service.postLimitedToken(limitedTokenRequest, accessToken);

    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenLogoutThenInvokeClient() {
    String accessToken = "accessToken";

    doNothing().when(clientMock).logout(CLIENT_ID,accessToken);

    Assertions.assertDoesNotThrow(() -> service.logout(accessToken));
  }

  @Test
  void whenRefreshTokenThenInvokeClient() {
    String refreshToken = "refreshToken";

    PostTokenRequest postTokenRequest = PostTokenRequest.builder()
      .clientId(CLIENT_ID)
      .grantType(GRANT_TYPE_REFRESH_TOKEN)
      .scope(SCOPE)
      .refreshToken(refreshToken)
      .build();
    AccessToken expectedResult = podamFactory.manufacturePojo(AccessToken.class);

    when(clientMock.postToken(postTokenRequest)).thenReturn(expectedResult);

    AccessToken result = service.refreshToken(refreshToken);

    Assertions.assertEquals(expectedResult, result);
  }
}
