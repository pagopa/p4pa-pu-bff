package it.gov.pagopa.pu.bff.service.pdnd_service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.PdndServiceService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndServiceRetrieverImplTest {

  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "fakeAccessToken";

  private static final UserInfo USER_INFO = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private PdndServiceService pdndServiceServiceMock;
  @InjectMocks
  private PdndServiceRetrieverImpl pdndServiceRetriever;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(
      authorizationServiceMock,
      pdndServiceServiceMock
    );
  }

  @Test
  void givenAuthorizedAdminWhenCreatePdndServiceThenReturnService() {
    PdndServiceRequestDTO pdndServiceRequestDTO = TestUtils.getPodamFactory().manufacturePojo(PdndServiceRequestDTO.class);
    PdndService expectedResult = TestUtils.getPodamFactory().manufacturePojo(PdndService.class);

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, USER_INFO);

    when(pdndServiceServiceMock.savePdndService(ORGANIZATION_ID, pdndServiceRequestDTO, SUB_UNIT_CODE, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    PdndService result = pdndServiceRetriever.createPdndService(ORGANIZATION_ID, pdndServiceRequestDTO, SUB_UNIT_CODE, USER_INFO, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenAuthorizedAdminWhenGetPdndServiceThenReturnService() {
    String purposeId = "PURPOSE_001";
    PdndServiceDTO expectedResult = TestUtils.getPodamFactory().manufacturePojo(PdndServiceDTO.class);

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, USER_INFO);

    when(pdndServiceServiceMock.getPdndService(ORGANIZATION_ID, purposeId, SUB_UNIT_CODE, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    PdndServiceDTO result = pdndServiceRetriever.getPdndService(ORGANIZATION_ID, purposeId, SUB_UNIT_CODE, USER_INFO, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenAuthorizedAdminWhenGetPdndServicesThenReturnServices() {
    List<PdndServiceDTO> expectedResult = List.of(TestUtils.getPodamFactory().manufacturePojo(PdndServiceDTO.class));

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, USER_INFO);

    when(pdndServiceServiceMock.getPdndServices(ORGANIZATION_ID, SUB_UNIT_CODE, PdndServiceType.SEND, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    List<PdndServiceDTO> result = pdndServiceRetriever.getPdndServices(ORGANIZATION_ID, SUB_UNIT_CODE, PdndServiceType.SEND, USER_INFO, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
