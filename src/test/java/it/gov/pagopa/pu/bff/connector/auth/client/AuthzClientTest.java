package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.auth.client.generated.AuthzApi;
import it.gov.pagopa.pu.auth.dto.generated.*;
import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    UserInfo result = authzClient.getUserInfoFromMappedExternalUserId(mappedExternalUserId,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentUserWhenGetUserInfoFromMappedExternalUserIdThenNull() {
    String mappedExternalUserId = "mappedExternalUserId";
    String accessToken = "ACCESSTOKEN";

    when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    when(authzApiMock.getUserInfoFromMappedExternaUserId(mappedExternalUserId))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    UserInfo result = authzClient.getUserInfoFromMappedExternalUserId(mappedExternalUserId,accessToken);

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

  @Test
  void whenGenerateClientSecretThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String clientId = "CLIENT_ID";

    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientId(clientId);

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);
    when(authzApiMock.generateClientSecret(organizationIpaCode, clientId)).thenReturn(expectedResult);

    ClientDTO result = authzClient.generateClientSecret(organizationIpaCode, clientId, accessToken);

    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void givenClientNotFoundWhenGenerateClientSecretThenThrowResourceNotFoundException() {
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String clientId = "CLIENT_ID";

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);

    doThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"))
      .when(authzApiMock).generateClientSecret(organizationIpaCode, clientId);

    NotFoundException thrown = assertThrows(NotFoundException.class, () -> authzClient.generateClientSecret(organizationIpaCode, clientId, accessToken));

    assertTrue(thrown.getMessage().contains(clientId));
  }

  @Test
  void givenIpaCodeAndMappedExternalUserIdWhenGetOrganizationOperatorThenReturnResult() {
    //given
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String mappedExternalUserId = "mappedExternalUserId";
    OperatorDTO expectedResult = new OperatorDTO();

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);
    when(authzApiMock.getOrganizationOperator(organizationIpaCode, mappedExternalUserId)).thenReturn(expectedResult);
    //when
    OperatorDTO result = authzClient.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void givenIpaCodeAndMappedExternalUserIdWhenGetOrganizationOperatorThenReturnNull() {
    //given
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String mappedExternalUserId = "mappedExternalUserId";

    when(authApisHolderMock.getAuthzApi(accessToken)).thenReturn(authzApiMock);
    doThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"))
      .when(authzApiMock).getOrganizationOperator(organizationIpaCode, mappedExternalUserId);

    //when
    OperatorDTO result = authzClient.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken);

    //then
    Assertions.assertNull(result);
  }
}
