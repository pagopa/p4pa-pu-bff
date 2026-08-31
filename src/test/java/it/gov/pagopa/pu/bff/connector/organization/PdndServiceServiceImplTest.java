package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.PdndServiceClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndServiceServiceImplTest {

  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "fakeAccessToken";

  @Mock
  private PdndServiceClient clientMock;

  @InjectMocks
  private PdndServiceServiceImpl service;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(clientMock);
  }

  @Test
  void givenPdndServiceRequestDTOWhenSavePdndServiceThenReturnSavedPdndService() {
    PdndServiceRequestDTO pdndServiceRequestDTO = new PdndServiceRequestDTO();
    PdndService expectedResult = new PdndService();

    when(clientMock.savePdndService(ORGANIZATION_ID, pdndServiceRequestDTO, SUB_UNIT_CODE, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    PdndService result = service.savePdndService(ORGANIZATION_ID, pdndServiceRequestDTO, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenOrganizationIdAndPurposeIdWhenGetPdndServiceThenReturnPdndService() {
    String purposeId = "PURPOSE_001";
    PdndServiceDTO expectedResult = new PdndServiceDTO();

    when(clientMock.getPdndService(ORGANIZATION_ID, purposeId, SUB_UNIT_CODE, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    PdndServiceDTO result = service.getPdndService(ORGANIZATION_ID, purposeId, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
