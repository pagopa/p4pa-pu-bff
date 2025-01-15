package it.gov.pagopa.pu.bff.service.authentication;

import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  public AuthenticationServiceImpl() {

  }

  public AccessTokenDTO postToken() {
    return null;
  }

  @Override
  public AccessTokenDTO postToken(String clientId, String grantType, String scope, String subjectTokenType, String clientSecret) {
    return null;
  }

}
