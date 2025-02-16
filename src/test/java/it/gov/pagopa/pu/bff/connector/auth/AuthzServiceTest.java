package it.gov.pagopa.pu.bff.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthzServiceTest {

  @Mock
  private AuthzClient client;

  private AuthzService service;

  @BeforeEach
  void setUp() {
    service = new AuthzServiceImpl(client);
  }

  @Test
  void whenGetUserInfoFromMappedExternalUserIdThenInvokeClient() {
    String mappedExternalUserId = "MAPPEDEXTERNALUSERID";
    String accessToken = "ACCESSTOKEN";
    UserInfo expectedResult = new UserInfo();

    when(client.getUserInfoFromMappedExternaUserId(Mockito.same(mappedExternalUserId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    UserInfo result = service.getUserInfoFromMappedExternaUserId(mappedExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }
}
