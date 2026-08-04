package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.PdndClientClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndClientServiceTest {

  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "fakeAccessToken";

  @Mock
  private PdndClientClient clientMock;

  @InjectMocks
  private PdndClientServiceImpl service;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(clientMock);
  }

  @Test
  void givenOrganizationIdAndSubUnitCodeWhenGetPdndClientsThenReturnClients() {
    List<PdndClientNoSecretDTO> expectedResult = List.of(new PdndClientNoSecretDTO());

    when(clientMock.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    List<PdndClientNoSecretDTO> result = service.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
