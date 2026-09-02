package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.pdnd_service.PdndServiceRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndServiceControllerTest {

  @Mock
  private PdndServiceRetrieverService pdndServiceRetrieverServiceMock;

  @InjectMocks
  private PdndServiceController controller;

  private static final Long ORGANIZATION_ID = 123L;
  private static final String ORG_SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String ACCESS_TOKEN = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(ACCESS_TOKEN, loggedUser);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(pdndServiceRetrieverServiceMock);
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenOrganizationIdAndPdndServiceRequestDTOWhenCreatePdndServiceThenReturnCreatedPdndService() {
    PdndServiceRequestDTO body = TestUtils.getPodamFactory().manufacturePojo(PdndServiceRequestDTO.class);
    PdndService expectedClient = TestUtils.getPodamFactory().manufacturePojo(PdndService.class);

    when(pdndServiceRetrieverServiceMock.createPdndService(ORGANIZATION_ID, body, ORG_SUB_UNIT_CODE, loggedUser, ACCESS_TOKEN))
      .thenReturn(expectedClient);

    ResponseEntity<PdndService> result = controller.createPdndService(ORGANIZATION_ID, body, ORG_SUB_UNIT_CODE);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertSame(expectedClient, result.getBody());
  }

  @Test
  void givenOrganizationIdPurposeIdAndSubUnitCodeWhenGetPdndServiceThenReturnPdndService() {
    String purposeId = "PURPOSE_001";
    PdndServiceDTO expectedService = TestUtils.getPodamFactory().manufacturePojo(PdndServiceDTO.class);

    when(pdndServiceRetrieverServiceMock.getPdndService(ORGANIZATION_ID, purposeId, ORG_SUB_UNIT_CODE, loggedUser, ACCESS_TOKEN))
      .thenReturn(expectedService);

    ResponseEntity<PdndServiceDTO> result = controller.getPdndService(ORGANIZATION_ID, purposeId, ORG_SUB_UNIT_CODE);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertSame(expectedService, result.getBody());
  }

  @Test
  void givenOrganizationIdAndServiceTypeAndSubUnitCodeWhenGetPdndServicesThenReturnPdndServices() {
    List<PdndServiceDTO> expectedResult = List.of(TestUtils.getPodamFactory().manufacturePojo(PdndServiceDTO.class));

    when(pdndServiceRetrieverServiceMock.getPdndServices(ORGANIZATION_ID, ORG_SUB_UNIT_CODE, PdndServiceType.SEND, loggedUser, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    ResponseEntity<List<PdndServiceDTO>> result = controller.getPdndServices(ORGANIZATION_ID, ORG_SUB_UNIT_CODE, PdndServiceType.SEND);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertSame(expectedResult, result.getBody());
  }

  @Test
  void whenGetPdndClientServicesThenReturnOk() {
    String clientId = "clientId";
    PdndServiceType serviceType = PdndServiceType.SEND;
    List<PdndService> expectedResult = List.of(TestUtils.getPodamFactory().manufacturePojo(PdndService.class));

    when(pdndServiceRetrieverServiceMock.getPdndClientServices(ORGANIZATION_ID, clientId, serviceType, loggedUser, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    ResponseEntity<List<PdndService>> result = controller.getPdndClientServices(ORGANIZATION_ID, clientId, serviceType);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertSame(expectedResult, result.getBody());
  }
}
