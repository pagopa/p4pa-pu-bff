package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceDecryptedDTO;
import it.gov.pagopa.pu.bff.dto.OrgSilServiceExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceRetrieverService;
import it.gov.pagopa.pu.bff.service.org_sil_service.OrgSilServiceStorerService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import lombok.extern.slf4j.Slf4j;
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
import uk.co.jemos.podam.api.PodamFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OrgSilServiceControllerTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrgSilServiceRetrieverService orgSilServiceRetrieverServiceMock;
  @Mock
  private OrgSilServiceStorerService orgSilServiceStorerServiceMock;

  @InjectMocks
  private OrgSilServiceController orgSilServiceController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      orgSilServiceRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetOrgSilServicesThenOk() {
    long organizationId = 1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;

    List<OrgSilServiceExtendedDTO> expectedResult = new ArrayList<>();
    expectedResult.add(new OrgSilServiceExtendedDTO());

    when(orgSilServiceRetrieverServiceMock.getOrgSilServices(organizationId, serviceType, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<List<OrgSilServiceExtendedDTO>> response = orgSilServiceController.getOrgSilServices(organizationId, serviceType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetOrgSilServicesByFiltersThenOk() {
    long organizationId = 1L;
    String applicationName = "myApp";
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    boolean flagLegacy = true;
    Pageable pageable = PageRequest.of(0, 10);

    PagedOrgSilServiceView expectedResult = new PagedOrgSilServiceView();

    when(orgSilServiceRetrieverServiceMock.getOrgSilServicesByFilters(organizationId, applicationName, serviceType, flagLegacy, pageable, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedOrgSilServiceView> response = orgSilServiceController.getOrgSilServicesByFilters(
      organizationId, applicationName, serviceType, flagLegacy, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetOrgSilServicesDetailsThenOk() {
    //given
    long organizationId = 2L;
    long orgSilServiceId = 1L;
    OrgSilServiceDecryptedDTO orgSilServiceDecryptedDTO = new OrgSilServiceDecryptedDTO();

    when(orgSilServiceRetrieverServiceMock.getOrgSilServiceDetails(organizationId, orgSilServiceId, loggedUser, accessToken)).thenReturn(orgSilServiceDecryptedDTO);
    //when
    ResponseEntity<OrgSilServiceDecryptedDTO> response = orgSilServiceController.getOrgSilServiceDetails(organizationId, orgSilServiceId);
    //then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(orgSilServiceDecryptedDTO, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetOrgSilServicesDetailsThenReturnNull() {
    //given
    long organizationId = 2L;
    long orgSilServiceId = 1L;

    when(orgSilServiceRetrieverServiceMock.getOrgSilServiceDetails(organizationId, orgSilServiceId, loggedUser, accessToken)).thenReturn(null);
    //when
    ResponseEntity<OrgSilServiceDecryptedDTO> response = orgSilServiceController.getOrgSilServiceDetails(organizationId, orgSilServiceId);
    //then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void givenCorrectRequestWhenCreateOrgSilServiceThenCreated() {
    long organizationId = 1L;

    OrgSilServiceDecryptedDTO requestBody = new OrgSilServiceDecryptedDTO();
    requestBody.setOrganizationId(organizationId);
    requestBody.setApplicationName("TestApp");
    requestBody.setServiceUrl("https://example.com/api");
    requestBody.setServiceType(OrgSilServiceType.ACTUALIZATION);
    requestBody.setFlagLegacy(false);

    OrgSilServiceDecryptedDTO expectedResponse = new OrgSilServiceDecryptedDTO();
    expectedResponse.setOrganizationId(organizationId);
    expectedResponse.setApplicationName("TestApp");
    expectedResponse.setServiceUrl("https://example.com/api");
    expectedResponse.setServiceType(OrgSilServiceType.ACTUALIZATION);
    expectedResponse.setFlagLegacy(false);

    when(orgSilServiceStorerServiceMock.createOrgSilService(organizationId, requestBody, loggedUser, accessToken))
      .thenReturn(expectedResponse);

    ResponseEntity<OrgSilServiceDecryptedDTO> response = orgSilServiceController.createOrgSilService(organizationId, requestBody);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(expectedResponse, response.getBody());
  }
}
