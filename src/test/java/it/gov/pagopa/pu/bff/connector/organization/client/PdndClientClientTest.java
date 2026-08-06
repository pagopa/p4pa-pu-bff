package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.controller.generated.PdndClientApi;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndClientClientTest {

  private static final String CLIENT_ID = "CLIENT_001";
  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "accessToken";

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;

  @Mock
  private PdndClientApi pdndClientApiMock;

  private PdndClientClient pdndClientClient;

  @BeforeEach
  void setUp() {
    pdndClientClient = new PdndClientClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      pdndClientApiMock
    );
  }

  @Test
  void givenOrganizationIdAndSubUnitCodeWhenGetPdndClientsThenReturnClients() {
    List<PdndClientNoSecretDTO> expectedResult = List.of(new PdndClientNoSecretDTO());

    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    when(pdndClientApiMock.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE))
      .thenReturn(expectedResult);

    List<PdndClientNoSecretDTO> result = pdndClientClient.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenOrganizationIdAndClientIdWhenGetPdndClientThenReturnClient() {
    PdndClientNoSecretDTO expectedResult = new PdndClientNoSecretDTO();

    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    when(pdndClientApiMock.getPdndClient(ORGANIZATION_ID, CLIENT_ID))
      .thenReturn(expectedResult);

    PdndClientNoSecretDTO result = pdndClientClient.getPdndClient(ORGANIZATION_ID, CLIENT_ID, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
