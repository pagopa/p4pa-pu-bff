package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.p4paauth.dto.generated.AccessToken;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class AuthorizationService {

  private final AuthnClient authClientImpl;
  private final String subjectIssuer;
  public static final String CLIENT_ID = "piattaforma-unitaria";
  public static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
  public static final String SCOPE = "openid";
  public static final String SUBJECT_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:jwt";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

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

  public void validateAdminRole(Long organizationId, UserInfo loggedUser) {
    boolean roleAdmin = loggedUser.getOrganizations().stream()
      .filter(o->organizationId.equals(o.getOrganizationId()))
        .findFirst()
        .filter(o ->!CollectionUtils.isEmpty(o.getRoles()) && o.getRoles().contains(ROLE_ADMIN))
        .isPresent();
    if(!roleAdmin){
      log.debug("Unauthorized user. [organizationId:{}]", organizationId);
      throw new AuthorizationDeniedException("Access denied on organizationId " + organizationId + " to user " + loggedUser.getMappedExternalUserId());
    }
  }
}
