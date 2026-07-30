package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.AccessToken;
import it.gov.pagopa.pu.auth.dto.generated.LimitedTokenRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

public interface AuthnService {
  UserInfo getUserInfo(String accessToken);
  AccessToken postToken(String subjectToken);
  AccessToken postLimitedToken(LimitedTokenRequest limitedTokenRequest, String accessToken);
  void logout(String accessToken);
  AccessToken refreshToken(String refreshToken);
}
