package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import org.springframework.stereotype.Service;

@Service
public class AuthzServiceImpl implements AuthzService {

  private final AuthzClient client;

  public AuthzServiceImpl(AuthzClient client) {
    this.client = client;
  }

  @Override
  public UserInfo getUserInfoFromMappedExternaUserId(String mappedExternalUserId, String accessToken) {
    return client.getUserInfoFromMappedExternaUserId(mappedExternalUserId, accessToken);
  }
}
