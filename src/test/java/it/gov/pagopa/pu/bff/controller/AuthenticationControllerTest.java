package it.gov.pagopa.pu.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock
  private AuthorizationService authorizationService;

  @InjectMocks
  private AuthenticationController authenticationController;

  private AccessToken accessTokenDTO;
  private UserInfo userInfo;

  @BeforeEach
  void setUp() {
    accessTokenDTO = new AccessToken();
    accessTokenDTO.setAccessToken("fake-access-token");
    accessTokenDTO.setExpiresIn(3600);
    accessTokenDTO.setTokenType("bearer");

    userInfo = new UserInfo();
    userInfo.setUserId("fakeUserId");
    userInfo.setMappedExternalUserId("fakeExternalId");
    userInfo.setFiscalCode("fakeFiscalCode");
    userInfo.setFamilyName("FakeFamilyName");
    userInfo.setName("FakeName");
    userInfo.setIssuer("fakeIssuer");
    userInfo.setCanManageUsers(true);

    Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void testPostToken() {
    String idToken = "validIdToken";

    when(authorizationService.postToken(idToken)).thenReturn(accessTokenDTO);

    ResponseEntity<AccessToken> response = authenticationController.postToken(idToken);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("fake-access-token", response.getBody().getAccessToken());
    assertEquals(3600, response.getBody().getExpiresIn());
    assertEquals("bearer", response.getBody().getTokenType());

    verify(authorizationService, times(1)).postToken(idToken);
  }

  @Test
  void testGetUserInfo() {
    ResponseEntity<UserInfo> response = authenticationController.getUserInfo();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userInfo,response.getBody());
  }

  @Test
  void testRevokeToken() {
    doNothing().when(authorizationService).logout("fakeAccessToken");

    ResponseEntity<Void> response = authenticationController.logout();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verifyNoMoreInteractions(authorizationService);
  }
}
