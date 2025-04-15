package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class AuthorizationService {
  public static final String CLIENT_ID = "piattaforma-unitaria";
  public static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
  public static final String SCOPE = "openid";
  public static final String SUBJECT_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:jwt";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

  private final AuthnClient authClientImpl;
  private final OrganizationService organizationService;
  private final String subjectIssuer;

  public AuthorizationService(@Value("${rest.auth.token-exchange-issuer}") String subjectIssuer,
                              AuthnClient authClientImpl,
    OrganizationService organizationService) {
    this.subjectIssuer = subjectIssuer;
    this.authClientImpl = authClientImpl;
    this.organizationService = organizationService;
  }

  public UserInfo validateToken(String accessToken) {
    log.info("Requesting validate token");
    return authClientImpl.getUserInfo(accessToken);
  }

  public AccessToken postToken(String idToken) {
    log.info("Posting token for validation");

    return authClientImpl.postToken(
      CLIENT_ID,
      GRANT_TYPE,
      SCOPE,
      idToken,
      subjectIssuer,
      SUBJECT_TOKEN_TYPE,
      null);
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

  private static AuthorizationDeniedException buildAuthorizationDeniedException(Long organizationId, UserInfo loggedUser){
    log.debug("Unauthorized user. [organizationId:{}]", organizationId);
    return new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId());
  }

  private static Optional<UserOrganizationRoles> getUserOrganizationRoles(Long organizationId, UserInfo loggedUser) {
    return loggedUser.getOrganizations().stream()
      .filter(o -> organizationId.equals(o.getOrganizationId()) && !CollectionUtils.isEmpty(o.getRoles()))
      .findFirst();
  }

  public void logout(String accessToken) {
    authClientImpl.logout(CLIENT_ID,accessToken);
  }

  public void validateOrganizationOrBrokerAdmin(Long organizationId, UserInfo loggedUser, String accessToken){
    if (!isOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken)) {
      throw new AuthorizationDeniedException(
        "User is neither the organization’s admin nor the broker’s admin, or the organization having organizationId "+organizationId+" is not managed by the broker having brokerId "+ loggedUser.getBrokerId());
    }
  }

  public boolean isOrganizationOrBrokerAdmin(Long organizationId, UserInfo loggedUser, String accessToken){
    return isAdminRole(organizationId,loggedUser) ||
      (isBrokerAdminRole(loggedUser)
        && isOrganizationHandledByBroker(organizationId,loggedUser,accessToken));
  }

  private boolean isOrganizationHandledByBroker(Long organizationId, UserInfo loggedUser, String accessToken) {
    Organization organization = organizationService.getOrganizationByOrganizationId(
      organizationId, accessToken);
    return loggedUser.getBrokerId()!=null && organization!=null && loggedUser.getBrokerId().equals(organization.getBrokerId());
  }
}
