package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import it.gov.pagopa.pu.bff.mapper.AccessTokenDTOMapper;
import it.gov.pagopa.pu.p4paauth.dto.generated.AccessToken;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

  @InjectMocks
  private AuthorizationService authorizationService;
  @Mock
  private AuthnClient authClientImplMock;
  @Mock
  private AccessTokenDTOMapper accessTokenDTOMapperMock;

  @Test
  void givenValidAccessTokenWhenValidateTokenThenOk() {
    UserInfo ui = new UserInfo();
    when(authClientImplMock.getUserInfo("ACCESSTOKEN")).thenReturn(ui);
    UserInfo result = authorizationService.validateToken("ACCESSTOKEN");

    Assertions.assertEquals(ui, result);
  }

  @Test
  void givenInvalidAccessTokenWhenValidateTokenThenInvalidAccessTokenException() {
    when(authClientImplMock.getUserInfo("INVALIDACCESSTOKEN")).thenThrow(new InvalidAccessTokenException("Bad Access Token provided"));
    InvalidAccessTokenException result = Assertions.assertThrows(InvalidAccessTokenException.class,
      () -> authorizationService.validateToken("INVALIDACCESSTOKEN"));

    Assertions.assertEquals("Bad Access Token provided", result.getMessage());
  }

  @Test
  void testPostToken() {
    ReflectionTestUtils.setField(authorizationService, "subjectIssuer", "fake-subject-issuer");

    String idToken = "idToken";
    String subjectIssuer = "fake-subject-issuer";

    AccessToken accessToken = new AccessToken();
    accessToken.setAccessToken("fake-access-token");
    accessToken.setExpiresIn(3600);
    accessToken.setTokenType("bearer");

    AccessTokenDTO expectedDto = new AccessTokenDTO();
    expectedDto.setAccessToken("fake-access-token");
    expectedDto.setTokenType("bearer");
    expectedDto.setExpiresIn(3600);

    when(authClientImplMock.postToken(
      "piattaforma-unitaria",
      "urn:ietf:params:oauth:grant-type:token-exchange",
      "openid",
      idToken,
      subjectIssuer,
      "urn:ietf:params:oauth:token-type:jwt",
      null))
      .thenReturn(accessToken);

    when(accessTokenDTOMapperMock.toDTO(accessToken)).thenReturn(expectedDto);

    AccessTokenDTO result = authorizationService.postToken(idToken);

    verify(authClientImplMock).postToken(
      "piattaforma-unitaria",
      "urn:ietf:params:oauth:grant-type:token-exchange",
      "openid",
      idToken,
      subjectIssuer,
      "urn:ietf:params:oauth:token-type:jwt",
      null);

    Assertions.assertEquals("fake-access-token", result.getAccessToken());
    Assertions.assertEquals("bearer", result.getTokenType());
    Assertions.assertEquals(3600, result.getExpiresIn());
  }

}


