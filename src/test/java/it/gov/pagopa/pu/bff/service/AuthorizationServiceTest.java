package it.gov.pagopa.pu.bff.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

  @InjectMocks
  private AuthorizationService authorizationService;
  @Mock
  private AuthnClient authClientImplMock;
  @Mock
  private OrganizationService organizationServiceMock;

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

    Mockito.doNothing().when(authClientImplMock).logout(AuthorizationService.CLIENT_ID,accessToken);

    authorizationService.logout(accessToken);

    Mockito.verifyNoMoreInteractions(authClientImplMock);
  }

  @Test
  void givenOrganizationAdminWhenIsOrganizationOrBrokerAdminThenTrue(){
    long organizationId = 1L;
    String accessToken = "accessToken";
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(organizationId);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole));

    boolean result = authorizationService.isOrganizationOrBrokerAdmin(organizationId,userInfo,
      accessToken);

    Assertions.assertTrue(result);
  }

  @Test
  void givenBrokersAdminAndOrganizationHandledByBrokerWhenIsOrganizationOrBrokerAdminThenTrue(){
    long organizationId = 2L;
    long brokerId = 3L;
    String accessToken = "accessToken";
    String brokerFiscalCode = "brokerFiscalCode";
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    userAdminRole.setOrganizationFiscalCode(brokerFiscalCode);
    UserOrganizationRoles userRole = new UserOrganizationRoles();
    userRole.setRoles(List.of("TEST"));
    userRole.setOrganizationId(organizationId);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userRole));
    userInfo.brokerFiscalCode(brokerFiscalCode);
    userInfo.setBrokerId(brokerId);

    Organization organization = new Organization();
    organization.setBrokerId(brokerId);

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    boolean result = authorizationService.isOrganizationOrBrokerAdmin(organizationId,userInfo,
      accessToken);

    Assertions.assertTrue(result);
  }

  @Test
  void givenNonAdminUserWhenIsOrganizationOrBrokerAdminThenFalse(){
    long organizationId = 2L;
    long brokerId = 3L;
    String accessToken = "accessToken";
    String orgFiscalCode = "orgFiscalCode";
    UserOrganizationRoles userRole = new UserOrganizationRoles();
    userRole.setRoles(List.of("TEST"));
    userRole.setOrganizationId(organizationId);
    userRole.setOrganizationFiscalCode(orgFiscalCode);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userRole));
    userInfo.brokerFiscalCode("brokerFiscalCode");
    userInfo.setBrokerId(brokerId);

    Organization organization = new Organization();
    organization.setBrokerId(brokerId);

    boolean result = authorizationService.isOrganizationOrBrokerAdmin(organizationId,userInfo,
      accessToken);

    Assertions.assertFalse(result);
    Mockito.verifyNoInteractions(organizationServiceMock);
  }

  @Test
  void givenBrokersAdminAndOrganizationNotHandledByBrokerWhenIsOrganizationOrBrokerAdminThenFalse(){
    long organizationId = 2L;
    long brokerId = 3L;
    String accessToken = "accessToken";
    String brokerFiscalCode = "brokerFiscalCode";
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    userAdminRole.setOrganizationFiscalCode(brokerFiscalCode);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole));
    userInfo.brokerFiscalCode(brokerFiscalCode);
    userInfo.setBrokerId(brokerId);
    Organization organization = new Organization();
    organization.setBrokerId(brokerId+1);

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    boolean result = authorizationService.isOrganizationOrBrokerAdmin(organizationId,userInfo,accessToken);

    Assertions.assertFalse(result);
  }

  @Test
  void givenOrganizationAdminWhenValidateOrganizationOrBrokerAdminThenOk(){
    long organizationId = 1L;
    String accessToken = "accessToken";
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(organizationId);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole));

    Assertions.assertDoesNotThrow(()->authorizationService.validateOrganizationOrBrokerAdmin(organizationId,userInfo,
      accessToken));
  }

  @Test
  void givenBrokersAdminAndOrganizationHandledByBrokerWhenValidateOrganizationOrBrokerAdminThenOk(){
    long organizationId = 2L;
    long brokerId = 3L;
    String accessToken = "accessToken";
    String brokerFiscalCode = "brokerFiscalCode";
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    userAdminRole.setOrganizationFiscalCode(brokerFiscalCode);
    UserOrganizationRoles userRole = new UserOrganizationRoles();
    userRole.setRoles(List.of("TEST"));
    userRole.setOrganizationId(organizationId);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole,userRole));
    userInfo.brokerFiscalCode(brokerFiscalCode);
    userInfo.setBrokerId(brokerId);

    Organization organization = new Organization();
    organization.setBrokerId(brokerId);

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    Assertions.assertDoesNotThrow(()->authorizationService.validateOrganizationOrBrokerAdmin(organizationId,userInfo,
      accessToken));
  }

  @Test
  void givenNonAdminUserWhenValidateOrganizationOrBrokerAdminThenAuthorizationDeniedException(){
    long organizationId = 2L;
    long brokerId = 3L;
    String accessToken = "accessToken";
    String orgFiscalCode = "orgFiscalCode";
    UserOrganizationRoles userRole = new UserOrganizationRoles();
    userRole.setRoles(List.of("TEST"));
    userRole.setOrganizationId(organizationId);
    userRole.setOrganizationFiscalCode(orgFiscalCode);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userRole));
    userInfo.brokerFiscalCode("brokerFiscalCode");
    userInfo.setBrokerId(brokerId);

    Organization organization = new Organization();
    organization.setBrokerId(brokerId);

    Assertions.assertThrows(AuthorizationDeniedException.class,()->authorizationService.validateOrganizationOrBrokerAdmin(organizationId,userInfo,
      accessToken));

    Mockito.verifyNoInteractions(organizationServiceMock);
  }

  @Test
  void givenBrokersAdminAndOrganizationNotHandledByBrokerWhenValidateOrganizationOrBrokerAdminThenAuthorizationDeniedException(){
    long organizationId = 2L;
    long brokerId = 3L;
    String accessToken = "accessToken";
    String brokerFiscalCode = "brokerFiscalCode";
    UserOrganizationRoles userAdminRole = new UserOrganizationRoles();
    userAdminRole.setRoles(List.of("TEST","ROLE_ADMIN"));
    userAdminRole.setOrganizationId(1L);
    userAdminRole.setOrganizationFiscalCode(brokerFiscalCode);
    UserInfo userInfo = new UserInfo();
    userInfo.setOrganizations(List.of(userAdminRole));
    userInfo.brokerFiscalCode(brokerFiscalCode);
    userInfo.setBrokerId(brokerId);
    Organization organization = new Organization();
    organization.setBrokerId(brokerId+1);

    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      authorizationService.validateOrganizationOrBrokerAdmin(organizationId,userInfo,accessToken));
  }
}


