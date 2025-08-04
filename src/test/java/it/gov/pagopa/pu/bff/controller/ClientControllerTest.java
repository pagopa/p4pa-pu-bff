package it.gov.pagopa.pu.bff.controller;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTO;
import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.ClientNoSecretDTO;
import it.gov.pagopa.pu.auth.dto.generated.CreateClientRequest;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.clients.ClientRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

  @Mock
  private ClientRetrieverService clientRetrieverServiceMock;

  @InjectMocks
  private ClientController clientController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      clientRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenGetClientsThenOk() {
    Long organizationId = 1L;
    Pageable pageRequest = PageRequest.of(4, 1);
    ClientNoSecretDTO clientNoSecretDTO = new ClientNoSecretDTO();
    clientNoSecretDTO.setOrganizationIpaCode("IPACODE");

    ClientDTOPage expectedResult = new ClientDTOPage();
    expectedResult.setContent(List.of(clientNoSecretDTO));
    Mockito.when(clientRetrieverServiceMock.getClients(organizationId, null, null, pageRequest, loggedUser, accessToken ))
      .thenReturn(expectedResult);

    ResponseEntity<ClientDTOPage> response = clientController.getClients(organizationId, null, null, pageRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getContent().size());
    assertEquals(expectedResult.getContent().getFirst().getOrganizationIpaCode(), response.getBody().getContent().getFirst().getOrganizationIpaCode());
  }

  @Test
  void whenRegisterClientThenSuccess() {
    // Given
    Long organizationId = 1L;
    String clientName = "CLIENTNAME";
    ClientDTO expectedResult = new ClientDTO();
    expectedResult.setClientName(clientName);

    CreateClientRequest request = new CreateClientRequest();
    request.clientName(clientName);

    Mockito.when(clientRetrieverServiceMock.registerClient(organizationId, request, loggedUser, accessToken ))
      .thenReturn(expectedResult);

    // When
    ResponseEntity<ClientDTO> response = clientController.registerClient(organizationId, request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedResult, response.getBody());
  }

  @Test
  void whenGetClientThenSuccess() {
    Long organizationId = 1L;
    String clientId = "CLIENT_ID";
    ClientDTO expectedClient = new ClientDTO();
    expectedClient.setClientId(clientId);

    Mockito.when(clientRetrieverServiceMock.getClient(organizationId, clientId, loggedUser, accessToken))
      .thenReturn(expectedClient);

    ResponseEntity<ClientDTO> response = clientController.getClient(organizationId, clientId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedClient, response.getBody());
  }
}
