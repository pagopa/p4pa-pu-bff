package it.gov.pagopa.pu.bff.service;

import static org.mockito.Mockito.mock;

import it.gov.pagopa.pu.bff.connector.AuthClientImpl;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
class AuthorizationServiceTest {

  @Autowired
  private AuthorizationService authorizationService;

  @Mock
  AuthClientImpl authClientImplMock;

  @BeforeEach
  void setUp(){
    authClientImplMock = mock(AuthClientImpl.class);
    authorizationService = new AuthorizationService(authClientImplMock);
  }

  @Test
  void givenValidAccessTokenWhenValidateTokenThenOk() {
    // When
    UserInfo ui = new UserInfo();
    Mockito.when(authClientImplMock.validateToken("ACCESSTOKEN")).thenReturn(ui);
    UserInfo result = authorizationService.validateToken("ACCESSTOKEN");

    // Then
    Assertions.assertEquals(
      ui,
      result
    );
  }

  @Test
  void givenInvalidAccessTokenWhenValidateTokenThenInvalidAccessTokenException() {
    // When
    Mockito.when(authClientImplMock.validateToken("INVALIDACCESSTOKEN")).thenThrow(new InvalidAccessTokenException("Bad Access Token provided"));
    InvalidAccessTokenException result = Assertions.assertThrows(InvalidAccessTokenException.class,
      () -> authorizationService.validateToken("INVALIDACCESSTOKEN"));

    // Then
    Assertions.assertEquals(
      "Bad Access Token provided",
      result.getMessage()
    );
  }


}
