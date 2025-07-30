package it.gov.pagopa.pu.bff.service.clients;

import it.gov.pagopa.pu.auth.dto.generated.ClientDTOPage;
import it.gov.pagopa.pu.auth.dto.generated.ClientNoSecretDTO;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;
import it.gov.pagopa.pu.bff.connector.auth.ClientService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.Collections;
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

@ExtendWith(MockitoExtension.class)
class ClientRetrieverServiceImplTest {

  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private ClientService clientServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;

  private ClientRetrieverService clientRetrieverService;

  private UserInfo userInfo;

  @BeforeEach
  void setUp() {
    UserOrganizationRoles userOrganizationRoles = new UserOrganizationRoles();
    userOrganizationRoles.setOrganizationIpaCode("testIpaCode");
    userOrganizationRoles.setRoles(Collections.singletonList("ROLE_ADMIN"));

    userInfo = new UserInfo();
    userInfo.setOrganizations(Collections.singletonList(userOrganizationRoles));
    userInfo.setBrokerId(1L);

    clientRetrieverService = new ClientRetrieverServiceImpl(clientServiceMock, authorizationServiceMock, organizationServiceMock);
  }

  @Test
  void givenOrganizationIdWhenGetClientsThenOk() {
    // Given
    Long organizationId = 1L;
    String accessToken = "TOKEN";
    Organization organization = new Organization();
    organization.setOrganizationId(organizationId);
    organization.setIpaCode("IPACODE");

    ClientDTOPage expectedResult = new ClientDTOPage();
    expectedResult.setContent(
      List.of(ClientNoSecretDTO.builder()
        .organizationIpaCode("IPACODE")
        .clientId("CLIENTID")
        .clientName("CLIENTNAME")
        .build()));
    Pageable pageRequest = PageRequest.of(1, 1);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, userInfo);
    Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,
        accessToken))
      .thenReturn(organization);
    Mockito.when(clientServiceMock.getClients(organization.getIpaCode(), null, null, pageRequest,
        accessToken))
      .thenReturn(expectedResult);

    // When
    ClientDTOPage result = clientRetrieverService.getClients(organizationId, null, null, pageRequest, userInfo,
      accessToken);

    // Then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

}
