package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.PdndServiceApi;
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
class PdndServiceClientTest {

  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "accessToken";

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private PdndServiceApi pdndServiceApiMock;

  @InjectMocks
  private PdndServiceClient pdndServiceClient;


  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(
      organizationApisHolderMock,
      pdndServiceApiMock
    );
  }

  @Test
  void givenPdndServiceRequestDTOWhenSavePdndServiceThenReturnSavedPdndService() {
    PdndServiceRequestDTO pdndServiceRequestDTO = new PdndServiceRequestDTO();
    PdndService expectedResult = new PdndService();

    when(organizationApisHolderMock.getPdndServiceApi(ACCESS_TOKEN))
      .thenReturn(pdndServiceApiMock);

    when(pdndServiceApiMock.savePdndService(ORGANIZATION_ID, pdndServiceRequestDTO, SUB_UNIT_CODE))
      .thenReturn(expectedResult);

    PdndService result = pdndServiceClient.savePdndService(ORGANIZATION_ID, pdndServiceRequestDTO, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenOrganizationIdAndPurposeIdWhenGetPdndServiceThenReturnPdndService() {
    String purposeId = "PURPOSE_001";
    PdndServiceDTO expectedResult = new PdndServiceDTO();

    when(organizationApisHolderMock.getPdndServiceApi(ACCESS_TOKEN))
      .thenReturn(pdndServiceApiMock);

    when(pdndServiceApiMock.getPdndService(ORGANIZATION_ID, purposeId, SUB_UNIT_CODE))
      .thenReturn(expectedResult);

    PdndServiceDTO result = pdndServiceClient.getPdndService(ORGANIZATION_ID, purposeId, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
