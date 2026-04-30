package it.gov.pagopa.pu.bff.connector.auth;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @Test
  void whenGetOrganizationOperatorsThenInvokeClient() {
    String organizationIpaCode = "IPACODE";
    String accessToken = "ACCESSTOKEN";
    OperatorsPage expectedResult = new OperatorsPage();

    when(client.getOrganizationOperators(organizationIpaCode, null, null, null, 0, 10, accessToken))
      .thenReturn(expectedResult);

    OperatorsPage result = service.getOrganizationOperators(organizationIpaCode, null, null, null, 0, 10, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetOrganizationOperatorThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String mappedExternalUserId = "mappedExternalUserId";
    OperatorDTO expectedResult = new OperatorDTO();

    when(client.getOrganizationOperator(organizationIpaCode, mappedExternalUserId,accessToken)).thenReturn(expectedResult);

    OperatorDTO result = service.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }
}
