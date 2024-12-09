package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.AuthClientImpl;
import it.gov.pagopa.pu.bff.exception.InvalidAccessTokenException;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
@Slf4j
public class AuthorizationService {

  private final AuthClientImpl authClientImpl;

  public AuthorizationService(AuthClientImpl authClientImpl) {
    this.authClientImpl = authClientImpl;
  }

  public UserInfo validateToken(String accessToken){
    log.info("Requesting validate token");
    try{
      return authClientImpl.validateToken(accessToken);
    } catch (HttpStatusCodeException ex){
      String errorMessage;
      if(HttpStatus.UNAUTHORIZED.equals(ex.getStatusCode())){
        errorMessage="Bad Access Token provided";
        log.info(errorMessage);
      } else {
        errorMessage="Something gone wrong while validate token";
        log.error(errorMessage, ex);
      }
      throw new InvalidAccessTokenException(errorMessage);
    }
  }
}
