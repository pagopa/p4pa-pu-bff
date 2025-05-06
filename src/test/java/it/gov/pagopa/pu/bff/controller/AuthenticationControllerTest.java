package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock
  private AuthorizationService authorizationServiceMock;

  @InjectMocks
  private AuthenticationController authenticationController;

  private AccessToken accessTokenDTO;
  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    accessTokenDTO = new AccessToken();
    accessTokenDTO.setAccessToken(accessToken);
    accessTokenDTO.setExpiresIn(3600);
    accessTokenDTO.setTokenType("bearer");

    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      authorizationServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void testPostToken() {
    String idToken = "validIdToken";

    when(authorizationServiceMock.postToken(idToken)).thenReturn(accessTokenDTO);

    ResponseEntity<AccessToken> response = authenticationController.postToken(idToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertSame(accessTokenDTO, response.getBody());
  }

  @Test
  void testGetUserInfo() {
    ResponseEntity<UserInfo> response = authenticationController.getUserInfo();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertSame(loggedUser,response.getBody());
  }

  @Test
  void testRevokeToken() {
    doNothing().when(authorizationServiceMock).logout(accessToken);

    ResponseEntity<Void> response = authenticationController.logout();

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
