package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;

public interface AuthClient {

  UserInfo validateToken(String accessToken);

}
