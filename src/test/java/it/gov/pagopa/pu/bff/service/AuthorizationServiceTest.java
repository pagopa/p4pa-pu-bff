package it.gov.pagopa.pu.bff.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

  @InjectMocks
  private AuthorizationService authorizationService;
  @Mock
  private AuthnClient authClientImplMock;

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

    when(authClientImplMock.postToken(
      "piattaforma-unitaria",
      "urn:ietf:params:oauth:grant-type:token-exchange",
      "openid",
      idToken,
      subjectIssuer,
      "urn:ietf:params:oauth:token-type:jwt",
      null))
      .thenReturn(accessToken);

    AccessToken result = authorizationService.postToken(idToken);

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

  @Test
  void givenAdminRoleWhenValidateAdminRoleThenOK() {
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    UserOrganizationRoles userTestRole = new UserOrganizationRoles();
    userTestRole.setRoles(List.of("TEST"));
    userTestRole.setOrganizationId(2L);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userTestRole));
    authorizationService.validateAdminRole(1L,userInfo);
  }

  @Test
  void givenNoAdminRoleWhenValidateAdminRoleThenAuthorizationDeniedException() {
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    UserOrganizationRoles userTestRole = new UserOrganizationRoles();
    userTestRole.setRoles(List.of("TEST"));
    userTestRole.setOrganizationId(2L);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userTestRole));
    userInfo.setMappedExternalUserId("externalUserId");
    AuthorizationDeniedException result = Assertions.assertThrows(
      AuthorizationDeniedException.class,
      () -> authorizationService.validateAdminRole(2L,userInfo));

    Assertions.assertEquals("Access denied on organizationId " + 2L + " to user externalUserId", result.getMessage());
  }

  @Test
  void givenAdminRoleWhenIsAdminRoleThenOK() {
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    UserOrganizationRoles userTestRole = new UserOrganizationRoles();
    userTestRole.setRoles(List.of("TEST"));
    userTestRole.setOrganizationId(2L);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userTestRole));
    boolean adminRole = AuthorizationService.isAdminRole(1L, userInfo);

    Assertions.assertTrue(adminRole);
  }

  @Test
  void givenNoAdminRoleWhenIsAdminRoleThenAuthorizationDeniedException() {
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    UserOrganizationRoles userTestRole = new UserOrganizationRoles();
    userTestRole.setRoles(List.of("TEST"));
    userTestRole.setOrganizationId(2L);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userTestRole));
    userInfo.setMappedExternalUserId("externalUserId");
    boolean adminRole = AuthorizationService.isAdminRole(2L, userInfo);

    Assertions.assertFalse(adminRole);
  }
}


