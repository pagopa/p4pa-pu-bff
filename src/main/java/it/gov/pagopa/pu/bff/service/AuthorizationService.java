package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.auth.client.AuthnClient;
import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import it.gov.pagopa.pu.bff.mapper.AccessTokenDTOMapper;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

  public AuthorizationService(@Value("${TOKEN_EXCHANGE_ISSUER}") String subjectIssuer,
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
    log.info("token: {}", idToken);

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

}
