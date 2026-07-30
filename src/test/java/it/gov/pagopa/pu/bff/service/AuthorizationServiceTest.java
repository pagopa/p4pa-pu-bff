package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.*;
import it.gov.pagopa.pu.bff.connector.auth.AuthnService;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.List;
import java.util.stream.Stream;

import static it.gov.pagopa.pu.bff.service.AuthorizationService.BFF_APP_NAME;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
  @InjectMocks
  private AuthorizationService authorizationService;
  @Mock
  private AuthnService authnServiceMock;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authnServiceMock);
  }

  @Test
  void givenValidAccessTokenWhenValidateTokenThenOk() {
    UserInfo ui = new UserInfo();
    when(authnServiceMock.getUserInfo("ACCESSTOKEN")).thenReturn(ui);
    UserInfo result = authorizationService.validateToken("ACCESSTOKEN");

    Assertions.assertEquals(ui, result);
  }

  @Test
  void givenInvalidAccessTokenWhenValidateTokenThenInvalidAccessTokenException() {
    when(authnServiceMock.getUserInfo("INVALIDACCESSTOKEN"))
      .thenThrow(new InvalidAccessTokenException(
        "INVALID_ACCESS_TOKEN",
        "The provided access token is invalid or expired"
      ));

    InvalidAccessTokenException result = Assertions.assertThrows(
      InvalidAccessTokenException.class,
      () -> authorizationService.validateToken("INVALIDACCESSTOKEN")
    );

    Assertions.assertEquals("INVALID_ACCESS_TOKEN", result.getCode());
    Assertions.assertEquals("The provided access token is invalid or expired", result.getMessage());
  }

  @Test
  void testPostToken() {
    String idToken = "idToken";

    AccessToken accessToken = new AccessToken();
    accessToken.setAccessToken("fake-access-token");
    accessToken.setExpiresIn(3600);
    accessToken.setTokenType("bearer");

    when(authnServiceMock.postToken(idToken)).thenReturn(accessToken);

    AccessToken result = authorizationService.postToken(idToken);

    Assertions.assertEquals("fake-access-token", result.getAccessToken());
    Assertions.assertEquals("bearer", result.getTokenType());
    Assertions.assertEquals(3600, result.getExpiresIn());
  }

  @Test
  void whenValidateBrokerAdminRoleThenOK() {
    String orgFiscalCode = "orgFiscalCode";

    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    userAdminRole.setOrganizationFiscalCode(orgFiscalCode);
    UserOrganizationRoles userTestRole = new UserOrganizationRoles();
    userTestRole.setRoles(List.of("TEST"));
    userTestRole.setOrganizationId(2L);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userTestRole));
    userInfo.setBrokerFiscalCode(orgFiscalCode);
    authorizationService.validateBrokerAdminRole(userInfo);
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
  void givenNoAdminRoleWhenValidateBrokerAdminRoleThenForbiddenException() {
    String orgFiscalCode = "orgFiscalCode";
    String adminOrgFiscalCode = "adminOrgFiscalCode";

    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST", "ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    userAdminRole.setOrganizationFiscalCode(adminOrgFiscalCode);
    UserOrganizationRoles userTestRole = new UserOrganizationRoles();
    userTestRole.setRoles(List.of("TEST"));
    userTestRole.setOrganizationId(2L);
    userTestRole.setOrganizationFiscalCode(orgFiscalCode);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userTestRole));
    userInfo.setMappedExternalUserId("externalUserId");
    userInfo.setBrokerFiscalCode(orgFiscalCode);

    Assertions.assertThrows(
      AuthorizationDeniedException.class,
      () -> authorizationService.validateBrokerAdminRole(userInfo));
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

  @Test
  void givenUserEnabledToOrganizationIdWhenIsUserEnabledThenOk() {
    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("TEST"));
    userOrgRole.setOrganizationId(1L);

    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userOrgRole));

    Assertions.assertDoesNotThrow(() -> AuthorizationService.validateUserForOrganizationId(1L, userInfo));
  }

  @Test
  void givenUserNotEnabledToOrganizationIdWhenIsUserEnabledThenUnauthorized() {
    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of("TEST"));
    userOrgRole.setOrganizationId(1L);

    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userOrgRole));

    Assertions.assertThrows(AuthorizationDeniedException.class, () -> AuthorizationService.validateUserForOrganizationId(2L, userInfo));
  }

  @Test
  void givenUserWithEmptyRolesWhenIsUserEnabledThenUnauthorized() {
    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(List.of());
    userOrgRole.setOrganizationId(1L);

    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userOrgRole));

    Assertions.assertThrows(AuthorizationDeniedException.class, () -> AuthorizationService.validateUserForOrganizationId(1L, userInfo));
  }

  @Test
  void givenUserWithNullRolesWhenIsUserEnabledThenUnauthorized() {
    UserOrganizationRoles userOrgRole = new UserOrganizationRoles();
    userOrgRole.setRoles(null);
    userOrgRole.setOrganizationId(1L);

    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userOrgRole));

    Assertions.assertThrows(AuthorizationDeniedException.class, () -> AuthorizationService.validateUserForOrganizationId(1L, userInfo));
  }

  @Test
  void whenLogoutThenOk(){
    String accessToken = "accessToken";

    Mockito.doNothing().when(authnServiceMock).logout(accessToken);

    Assertions.assertDoesNotThrow(() -> authorizationService.logout(accessToken));
  }

  @Test
  void whenPostLimitedTokenThenOk() {
    // Given
    Long organizationId = 1L;
    AccessToken expectedAccessToken = new AccessToken();
    expectedAccessToken.setAccessToken("access-token");

    when(authnServiceMock.postLimitedToken(LimitedTokenRequest.builder()
      .app("APP").resource("p4pa-pu-bff").organizationId(organizationId).build(), "ACCESSTOKEN"))
      .thenReturn(expectedAccessToken);
    // When
    AccessToken result = authorizationService.postLimitedToken(organizationId, "APP", "ACCESSTOKEN");
    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedAccessToken, result);
  }

  @Test
  void givenUserHasRoleForOrganizationWhenValidateLimitedScopeUserForResourceThenOk() {
    Long organizationId = 1L;
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("ROLE_ADMIN"));
    userAdminRole.setOrganizationId(organizationId);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole));

    Assertions.assertDoesNotThrow(() -> authorizationService.validateLimitedScopeUserForResource(organizationId,userInfo, "resource","resourceId"));
  }

  @Test
  void givenUserHasNoRoleForOrganizationWhenValidateLimitedScopeUserForResourceThenOk() {
    Long organizationId = 1L;
    String resource = "resource";
    String resourceId = "resourceId";
    UserOrganizationRoles userOrganizationRole = new UserOrganizationRoles();
    userOrganizationRole.setOrganizationId(organizationId);
    LimitedScopeResource  limitedScopeResource = new LimitedScopeResource();
    limitedScopeResource.setApp(BFF_APP_NAME);
    limitedScopeResource.setResource(resource);
    limitedScopeResource.setOrganization(userOrganizationRole);
    limitedScopeResource.setResourceId(resourceId);
    UserInfoLimitedScope userInfo = new UserInfoLimitedScope();
    userInfo.setResource(limitedScopeResource);

    Assertions.assertDoesNotThrow(() -> authorizationService.validateLimitedScopeUserForResource(organizationId,userInfo, resource, resourceId));
  }

  @ParameterizedTest
  @MethodSource("limitedScopeResourceSource")
  void givenUserHasNoRoleForOrganizationAndNoMatchingLimitedScopeWhenValidateLimitedScopeUserForResourceThenOk(Long parameterizedOrganizationId, String parameterizedResourceStr, String parameterizedResourceId, String parameterizedAppName) {
    Long organizationId = 1L;
    String resource = "resource";
    String resourceId = "resourceId";
    UserOrganizationRoles userOrganizationRole = new UserOrganizationRoles();
    userOrganizationRole.setOrganizationId(parameterizedOrganizationId);
    LimitedScopeResource  limitedScopeResource = new LimitedScopeResource();
    limitedScopeResource.setApp(parameterizedAppName);
    limitedScopeResource.setResource(parameterizedResourceStr);
    limitedScopeResource.setOrganization(userOrganizationRole);
    limitedScopeResource.setResourceId(parameterizedResourceId);
    UserInfoLimitedScope userInfo = new UserInfoLimitedScope();
    userInfo.setResource(limitedScopeResource);

    AuthorizationDeniedException authorizationDeniedException = Assertions.assertThrows(AuthorizationDeniedException.class, () -> authorizationService.validateLimitedScopeUserForResource(organizationId, userInfo, resource, resourceId));

    Assertions.assertTrue(authorizationDeniedException.getMessage().startsWith("[USER_UNAUTHORIZED]"));
  }

  static Stream<Arguments> limitedScopeResourceSource() {
    long organizationId = 1L;
    String resource = "resource";
    String resourceId = "resourceId";
    return Stream.of(
      Arguments.of(organizationId+1,resource,resourceId,BFF_APP_NAME),
      Arguments.of(organizationId,resource+1,resourceId,BFF_APP_NAME),
      Arguments.of(organizationId,resource,resourceId+1,BFF_APP_NAME),
      Arguments.of(organizationId,resource,resourceId,BFF_APP_NAME+1)

    );
  }

  @Test
  void whenRefreshTokenThenOk(){
    String refreshToken = "ACCESSTOKEN";

    AccessToken expectedResult = new AccessToken();
    expectedResult.setAccessToken("fake-access-token");
    expectedResult.setExpiresIn(3600);
    expectedResult.setTokenType("bearer");

    when(authnServiceMock.refreshToken(refreshToken)).thenReturn(expectedResult);

    AccessToken result = authorizationService.refreshToken(refreshToken);

    Assertions.assertEquals(expectedResult, result);
  }
}


