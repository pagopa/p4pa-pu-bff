package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.controller.generated.AuthzApi;
import it.gov.pagopa.pu.auth.dto.generated.*;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthzClientTest {

  @Mock
  private AuthApisHolder authApisHolderMock;
  @Mock
  private AuthzApi authzApiMock;

  private AuthzClient authzClient;

  @BeforeEach
  void setUp() {
    authzClient = new AuthzClient(authApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authApisHolderMock
    );
  }

  @Test
  void whenGetUserInfoFromMappedExternalUserIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String mappedExternalUserId = "mappedExternalUserId";
    UserInfo expectedResult = new UserInfo();

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenReturn(expectedResult);

    UserInfo result = authzClient.getUserInfoFromMappedExternaUserId(mappedExternalUserId,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentUserWhenGetUserInfoFromMappedExternalUserIdThenNull() {
    String mappedExternalUserId = "mappedExternalUserId";
    String accessToken = "ACCESSTOKEN";

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    UserInfo result = authzClient.getUserInfoFromMappedExternaUserId(mappedExternalUserId,accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenGetOrganizationOperatorsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    OperatorsPage expectedResult = new OperatorsPage();

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getOrganizationOperators(organizationIpaCode, null, null, null, 0, 10))
      .thenReturn(expectedResult);

    OperatorsPage result = authzClient.getOrganizationOperators(organizationIpaCode, null, null, null, 0, 10, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetClientsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    ClientDTOPage expectedResult = new ClientDTOPage();

    Pageable pageRequest = PageRequest.of(4, 1);

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getClientsSearch(organizationIpaCode, null, null,
      pageRequest.getPageNumber(), pageRequest.getPageSize(), PageUtils.getSortList(pageRequest)))
      .thenReturn(expectedResult);

    ClientDTOPage result = authzClient.getClients(organizationIpaCode, null, null, pageRequest, accessToken );

    assertSame(expectedResult, result);
  }

  @Test
  void whenRegisterClientThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String clientName = "CLIENTNAME";
    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientName(clientName);

    CreateClientRequest request = new CreateClientRequest();
    request.clientName(clientName);

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);
    when(authzApiMock.registerClient(organizationIpaCode, request)).thenReturn(expectedResult);

    // When
    ClientDTO result = authzClient.registerClient(organizationIpaCode, request, accessToken);

    // Then
    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void whenGetClientThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String clientId = "CLIENT_ID";

    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientId(clientId);

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);
    when(authzApiMock.getClient(organizationIpaCode, clientId)).thenReturn(expectedResult);

    ClientDTO result = authzClient.getClient(organizationIpaCode, clientId, accessToken);

    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void whenRevokeClientThenInvokeWithAccessToken() {
    //given
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String clientId = "CLIENTID";

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);
    doNothing().when(authzApiMock).revokeClient(organizationIpaCode, clientId);
    //when
    authzClient.revokeClient(organizationIpaCode,clientId, accessToken);
    //then
    Mockito.verifyNoMoreInteractions(authzApiMock);
  }
}
