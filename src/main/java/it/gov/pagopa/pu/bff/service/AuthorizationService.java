package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.AuthClientImpl;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorizationService {

  private final AuthClientImpl authClientImpl;

  public AuthorizationService(AuthClientImpl authClientImpl) {
    this.authClientImpl = authClientImpl;
  }

  public UserInfo validateToken(String accessToken){
    log.info("Requesting validate token");
    return authClientImpl.validateToken(accessToken);
  }
}
