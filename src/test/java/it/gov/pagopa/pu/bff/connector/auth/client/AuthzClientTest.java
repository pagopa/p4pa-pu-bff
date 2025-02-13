package it.gov.pagopa.pu.bff.connector.auth.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.controller.generated.AuthzApi;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
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

@ExtendWith(MockitoExtension.class)
class AuthzClientTest {
  @Mock
  private AuthApisHolder authApisHolderMock;
  @Mock
  private AuthzApi authzApiMock;

  private AuthzClient authzClient;

  @BeforeEach
  void setUp() {
    authzClient = new AuthzClient(authApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authApisHolderMock
    );
  }

  @Test
  void whenGetUserInfoFromMappedExternalUserIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String mappedExternalUserId = "mappedExternalUserId";
    UserInfo expectedResult = new UserInfo();

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenReturn(expectedResult);

    UserInfo result = authzClient.getUserInfoFromMappedExternalUserId(mappedExternalUserId,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentUserWhenGetUserInfoFromMappedExternalUserIdThenNull() {
    String mappedExternalUserId = "mappedExternalUserId";
    String accessToken = "ACCESSTOKEN";

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    UserInfo result = authzClient.getUserInfoFromMappedExternalUserId(mappedExternalUserId,accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenHttpExceptionWhenGetUserInfoFromMappedExternalUserIdThenThrowIt() {
    String accessToken = "ACCESSTOKEN";
    String mappedExternalUserId = "mappedExternalUserId";
    HttpClientErrorException expectedException = new HttpClientErrorException(
      HttpStatus.INTERNAL_SERVER_ERROR);

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(),
      () -> authzClient.getUserInfoFromMappedExternalUserId(mappedExternalUserId,accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetUserInfoFromMappedExternalUserIdThenThrowIt() {
    String accessToken = "ACCESSTOKEN";
    String mappedExternalUserId = "mappedExternalUserId";
    RuntimeException expectedException = new RuntimeException();

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(expectedException.getClass(),
      () -> authzClient.getUserInfoFromMappedExternalUserId(mappedExternalUserId,accessToken));

    Assertions.assertSame(expectedException, result);
  }

}
