package it.gov.pagopa.pu.bff.connector.auth;


import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.ClientNoSecretDTO;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

  @Mock
  private AuthzClient authzClientMock;

  private ClientService clientService;

  @BeforeEach
  void setUp() {
    clientService = new ClientServiceImpl(authzClientMock);
  }

  @Test
  void whenGetClientsThenSuccess(){
    // Given
    String accessToken = "ACCESSTOKEN";
    Pageable pageRequest = PageRequest.of(1, 1);

    ClientDTOPage expectedResult = new ClientDTOPage();
    expectedResult.setContent(
      List.of(ClientNoSecretDTO.builder()
        .organizationIpaCode("IPACODE")
        .clientId("CLIENTID")
        .clientName("CLIENTNAME")
        .build()));

    Mockito.when(authzClientMock.getClients("IPACODE", null, null, pageRequest, accessToken))
      .thenReturn(expectedResult);

    // When
    ClientDTOPage result = clientService.getClients("IPACODE", null, null, pageRequest, accessToken);

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenRegisterClientThenSuccess(){
    // Given
    String accessToken = "ACCESSTOKEN";
    String ipaCode = "IPACODE";
    String clientName = "CLIENTNAME";

    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientName(clientName);

    CreateClientRequest request = new CreateClientRequest();
    request.setClientName(clientName);

    Mockito.when(authzClientMock.registerClient(ipaCode, request, accessToken)).thenReturn(expectedResult);

    // When
    ClientDTO result = clientService.registerClient(ipaCode, request, accessToken);

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenGetClientThenSuccess() {
    String accessToken = "ACCESSTOKEN";
    String ipaCode = "IPACODE";
    String clientId = "CLIENT_ID";

    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientId(clientId);

    Mockito.when(authzClientMock.getClient(ipaCode, clientId, accessToken)).thenReturn(expectedResult);

    ClientDTO result = clientService.getClient(ipaCode, clientId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenDeleteClientThenSuccess() {
    //given
    String accessToken = "ACCESSTOKEN";
    String organizationIpaCode = "IPACODE";
    String clientId = "CLIENTID";

    Mockito.doNothing().when(authzClientMock).revokeClient(organizationIpaCode, clientId, accessToken);
    //when
    clientService.deleteClient(organizationIpaCode, clientId, accessToken);
    //then
    verifyNoMoreInteractions(authzClientMock);
  }

  @Test
  void whenGenerateClientSecretThenSuccess() {
    String accessToken = "ACCESSTOKEN";
    String ipaCode = "IPACODE";
    String clientId = "CLIENT_ID";

    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientId(clientId);

    Mockito.when(authzClientMock.generateClientSecret(ipaCode, clientId, accessToken))
      .thenReturn(expectedResult);

    ClientDTO result = clientService.generateClientSecret(ipaCode, clientId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }
}
