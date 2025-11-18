package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.UserInfoDTO;
import it.gov.pagopa.pu.bff.mapper.UserInfoDTOMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private UserInfoDTOMapper userInfoDTOMapperMock;

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
      authorizationServiceMock,
      userInfoDTOMapperMock
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
    Mockito.when(userInfoDTOMapperMock.mapToDTO(loggedUser)).thenReturn(UserInfoDTO.builder().userId("test").build());

    ResponseEntity<UserInfoDTO> response = authenticationController.getUserInfo();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("test", response.getBody().getUserId());
  }

  @Test
  void testRevokeToken() {
    doNothing().when(authorizationServiceMock).logout(accessToken);

    ResponseEntity<Void> response = authenticationController.logout();

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
