package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.*;
import it.gov.pagopa.pu.bff.connector.auth.AuthnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Service
@Slf4j
public class AuthorizationService {
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String BFF_APP_NAME = "p4pa-pu-bff";

  private final AuthnService authnService;

  public AuthorizationService(AuthnService authnService) {
    this.authnService = authnService;
  }

  public UserInfo validateToken(String accessToken) {
    log.info("Requesting validate token");
    return authnService.getUserInfo(accessToken);
  }

  public AccessToken postToken(String idToken) {
    return authnService.postToken(idToken);
  }

  public AccessToken postLimitedToken(Long organizationId, String app, String accessToken) {
    return authnService.postLimitedToken(LimitedTokenRequest.builder()
      .organizationId(organizationId)
      .resource(BFF_APP_NAME)
      .app(app)
      .build(),
      accessToken);
  }

  public void validateBrokerAdminRole(UserInfo loggedUser) {
    if (!isBrokerAdminRole(loggedUser)) {
      log.debug(
        "Broker is not an admin for this organization. [user brokerFiscalCode:{}]",
        loggedUser.getBrokerFiscalCode());
      throw new AuthorizationDeniedException(
        "Access forbidden on broker to user "
          + loggedUser.getMappedExternalUserId());
    }
  }

  private static boolean isBrokerAdminRole(UserInfo loggedUser) {
    String brokerFiscalCode = loggedUser.getBrokerFiscalCode();
    return loggedUser.getOrganizations()
      .stream()
      .anyMatch(o ->
        o.getOrganizationFiscalCode().equals(brokerFiscalCode) &&
          !CollectionUtils.isEmpty(o.getRoles()) &&
          o.getRoles().contains(ROLE_ADMIN));
  }

  public void validateAdminRole(Long organizationId, UserInfo loggedUser) {
    boolean roleAdmin = isAdminRole(organizationId, loggedUser);
    if (!roleAdmin) {
      throw buildAuthorizationDeniedException(organizationId, loggedUser);
    }
  }

  public static boolean isAdminRole(Long organizationId, UserInfo loggedUser) {
    return getUserOrganizationRoles(organizationId, loggedUser)
      .filter(o -> !CollectionUtils.isEmpty(o.getRoles()) && o.getRoles()
        .contains(ROLE_ADMIN))
      .isPresent();
  }

  public static void validateUserForOrganizationId(Long organizationId, UserInfo loggedUser) {
    if (getUserOrganizationRoles(organizationId, loggedUser).isEmpty()) {
      throw buildAuthorizationDeniedException(organizationId, loggedUser);
    }
  }

  public static AuthorizationDeniedException buildAuthorizationDeniedException(Long organizationId, UserInfo loggedUser){
    log.debug("Unauthorized user. [organizationId:{}]", organizationId);
    return new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId());
  }

  private static Optional<UserOrganizationRoles> getUserOrganizationRoles(Long organizationId, UserInfo loggedUser) {
    return loggedUser.getOrganizations().stream()
      .filter(o -> organizationId.equals(o.getOrganizationId()) && !CollectionUtils.isEmpty(o.getRoles()))
      .findFirst();
  }

  public void logout(String accessToken) {
    authnService.logout(accessToken);
  }

  public void validateLimitedScopeUserForResource(Long organizationId, UserInfo loggedUser, String resource, String resourceId) {
    if(getUserOrganizationRoles(organizationId, loggedUser).isPresent()){
      return;
    }
    if(!limitedScopeUserHasAccessToResource(organizationId, loggedUser, resource, resourceId)) {
      throw new AuthorizationDeniedException("[USER_UNAUTHORIZED] Access denied on organizationId " + organizationId + " and resource "+resource+" having id " +resourceId+" to user " + loggedUser.getMappedExternalUserId());
    }
  }

  private boolean limitedScopeUserHasAccessToResource(Long organizationId, UserInfo loggedUser, String resource, String resourceId) {
    return loggedUser instanceof UserInfoLimitedScope userInfoLimitedScope
      && userInfoLimitedScope.getResource().getResource().equals(resource)
      && userInfoLimitedScope.getResource().getApp().equals(BFF_APP_NAME)
      && userInfoLimitedScope.getResource().getOrganization().getOrganizationId().equals(organizationId)
      && userInfoLimitedScope.getResource().getResourceId().equals(resourceId);
  }

  public AccessToken refreshToken(String refreshToken) {
    return authnService.refreshToken(refreshToken);
  }
}
