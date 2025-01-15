package it.gov.pagopa.pu.bff.service.authentication;

import it.gov.pagopa.pu.bff.dto.generated.AccessTokenDTO;

public interface AuthenticationService {

  AccessTokenDTO postToken(String clientId, String grantType, String scope, String subjectTokenType, String clientSecret);

}
