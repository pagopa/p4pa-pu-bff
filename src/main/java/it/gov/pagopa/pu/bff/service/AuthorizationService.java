package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import it.gov.pagopa.pu.bff.mapper.AccessTokenDTOMapper;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class AuthorizationService {

  private final AccessTokenDTOMapper accessTokenDTOMapper;
  private final AuthnClient authClientImpl;
  private final String subjectIssuer;
  public static final String CLIENT_ID = "piattaforma-unitaria";
  public static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
  public static final String SCOPE = "openid";
  public static final String SUBJECT_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:jwt";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

  public AuthorizationService(@Value("${rest.auth.token-exchange-issuer}") String subjectIssuer,
                              AccessTokenDTOMapper accessTokenDTOMapper,
                              AuthnClient authClientImpl) {
    this.subjectIssuer = subjectIssuer;
    this.accessTokenDTOMapper = accessTokenDTOMapper;
    this.authClientImpl = authClientImpl;
  }

  public UserInfo validateToken(String accessToken) {
    log.info("Requesting validate token");
    return authClientImpl.getUserInfo(accessToken);
  }

  public AccessTokenDTO postToken(String idToken) {
    log.info("Posting token for validation");

    return accessTokenDTOMapper.toDTO(
      authClientImpl.postToken(
        CLIENT_ID,
        GRANT_TYPE,
        SCOPE,
        idToken,
        subjectIssuer,
        SUBJECT_TOKEN_TYPE,
        null));
  }

  public void validateAdminRole(Long organizationId, UserInfo loggedUser) {
    boolean roleAdmin = loggedUser.getOrganizations().stream()
      .filter(o->!CollectionUtils.isEmpty(o.getRoles()))
      .anyMatch(o ->
        organizationId.equals(o.getOrganizationId())
        && o.getRoles().contains(ROLE_ADMIN));
    if(!roleAdmin){
      log.debug("Unauthorized user. [organizationId:{}]", organizationId);
      throw new AuthorizationDeniedException("Access Denied");
    }
  }
}
