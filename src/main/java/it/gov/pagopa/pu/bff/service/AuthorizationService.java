package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
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
  private final String subjectIssuer;

  public AuthorizationService(@Value("${rest.auth.token-exchange-issuer}") String subjectIssuer,
                              AuthnClient authClientImpl) {
    this.subjectIssuer = subjectIssuer;
    this.authClientImpl = authClientImpl;
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
    String brokerFiscalCode = loggedUser.getBrokerFiscalCode();
    boolean isBrokerAdmin = loggedUser.getOrganizations()
      .stream()
      .anyMatch(o ->
        brokerFiscalCode.equals(o.getOrganizationFiscalCode()) &&
          !CollectionUtils.isEmpty(o.getRoles()) &&
          o.getRoles().contains(ROLE_ADMIN));

    if (!isBrokerAdmin) {
      log.debug(
        "Broker is not an admin for this organization. [user brokerFiscalCode:{}]",
        loggedUser.getBrokerFiscalCode());
      throw new AuthorizationDeniedException(
        "Access forbidden on broker to user "
          + loggedUser.getMappedExternalUserId());
    }
  }

  public void validateAdminRole(Long organizationId, UserInfo loggedUser) {
    boolean roleAdmin = isAdminRole(organizationId, loggedUser);
    if (!roleAdmin) {
      handleUnauthorizedUser(organizationId, loggedUser);
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
      handleUnauthorizedUser(organizationId, loggedUser);
    }
  }

  private static void handleUnauthorizedUser(Long organizationId, UserInfo loggedUser) {
    log.debug("Unauthorized user. [organizationId:{}]", organizationId);
    throw new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId());
  }

  private static Optional<UserOrganizationRoles> getUserOrganizationRoles(Long organizationId, UserInfo loggedUser) {
    return loggedUser.getOrganizations().stream()
      .filter(o -> organizationId.equals(o.getOrganizationId()) && !CollectionUtils.isEmpty(o.getRoles()))
      .findFirst();
  }

  public void logout(String accessToken) {
    authClientImpl.logout(CLIENT_ID,accessToken);
  }
}
