package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

public interface AuthzService {
  UserInfo getUserInfoFromMappedExternaUserId(String mappedExternalUserId, String accessToken);
}
