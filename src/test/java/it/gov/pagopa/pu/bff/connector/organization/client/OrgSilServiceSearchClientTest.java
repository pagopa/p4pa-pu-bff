package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.controller.generated.OrgSilServiceEntityControllerApi;
import it.gov.pagopa.pu.organization.controller.generated.OrgSilServiceSearchControllerApi;
import it.gov.pagopa.pu.organization.controller.generated.OrgSilServiceViewSearchControllerApi;
import it.gov.pagopa.pu.organization.controller.generated.OrganizationSilServiceApi;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSilServiceView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSilServiceSearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrgSilServiceSearchControllerApi orgSilServiceSearchControllerApiMock;
  @Mock
  private OrgSilServiceEntityControllerApi orgSilServiceEntityControllerApiMock;
  @Mock
  private OrganizationSilServiceApi organizationSilServiceApiMock;
  @Mock
  private OrgSilServiceViewSearchControllerApi orgSilServiceViewSearchControllerApiMock;

  private OrgSilServiceSearchClient orgSilServiceSearchClient;

  @BeforeEach
  void setUp() {
    orgSilServiceSearchClient = new OrgSilServiceSearchClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(organizationApisHolderMock, orgSilServiceSearchControllerApiMock, orgSilServiceEntityControllerApiMock, orgSilServiceViewSearchControllerApiMock, organizationSilServiceApiMock);
  }

  @Test
  void whenGetOrgSilServicesThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    CollectionModelOrgSilService expectedResponse = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);

    when(organizationApisHolderMock.getOrgSilServiceSearchControllerApi(accessToken))
      .thenReturn(orgSilServiceSearchControllerApiMock);
    when(orgSilServiceSearchControllerApiMock.crudOrgSilServicesFindAllByOrganizationIdAndServiceType(organizationId, serviceType)).thenReturn(
      expectedResponse);

    CollectionModelOrgSilService response = orgSilServiceSearchClient.getOrgSilServices(organizationId, serviceType, accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }

  @Test
  void whenGetOrgSilServiceByIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long orgSilServiceId = 1L;
    OrgSilService expectedResponse = podamFactory.manufacturePojo(OrgSilService.class);

    when(organizationApisHolderMock.getOrgSilServiceEntityControllerApi(accessToken))
      .thenReturn(orgSilServiceEntityControllerApiMock);
    when(orgSilServiceEntityControllerApiMock.crudGetOrgsilservice(String.valueOf(orgSilServiceId)))
      .thenReturn(expectedResponse);

    OrgSilService response = orgSilServiceSearchClient.getOrgSilServiceById(orgSilServiceId, accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }

  @Test
  void givenNonExistentOrgSilServiceIdIdWhenGetOrgSilServiceByIdThenReturnNull() {
    Long orgSilServiceId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolderMock.getOrgSilServiceEntityControllerApi(accessToken))
      .thenReturn(orgSilServiceEntityControllerApiMock);
    when(orgSilServiceEntityControllerApiMock.crudGetOrgsilservice(String.valueOf(orgSilServiceId)))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    OrgSilService response = orgSilServiceSearchClient.getOrgSilServiceById(orgSilServiceId, accessToken);

    assertNull(response);
  }

  @Test
  void whenGetOrgSilServicesByFiltersThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String applicationName = "myApp";
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    boolean flagLegacy = true;
    Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
    PagedModelOrgSilServiceView expectedResponse =
      podamFactory.manufacturePojo(PagedModelOrgSilServiceView.class);

    when(organizationApisHolderMock.getOrgSilServiceViewSearchControllerApi(accessToken))
      .thenReturn(orgSilServiceViewSearchControllerApiMock);

    when(orgSilServiceViewSearchControllerApiMock.crudOrgSilServicesViewFindOrgSilServicesByFilters(
      organizationId,
      applicationName,
      serviceType,
      flagLegacy,
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResponse);

    PagedModelOrgSilServiceView response =
      orgSilServiceSearchClient.getOrgSilServicesByFilters(
        organizationId,
        applicationName,
        serviceType,
        flagLegacy,
        pageable,
        accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }

  @Test
  void givenIdWhenGetOrgSilServiceByIdDecryptedThenInvokeWithAccessToken() {
    //given
    Long orgSilServiceId = 1L;
    String accessToken = "ACCESSTOKEN";
    OrgSilServiceDTO orgSilService = podamFactory.manufacturePojo(OrgSilServiceDTO.class);

    when(organizationApisHolderMock.getOrganizationSilServiceApi(accessToken)).thenReturn(organizationSilServiceApiMock);
    when(organizationSilServiceApiMock.getOrgSilService(orgSilServiceId)).thenReturn(orgSilService);
    //when
    OrgSilServiceDTO result = orgSilServiceSearchClient.getOrgSilServiceByIdDecrypted(orgSilServiceId, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(orgSilService, result);
  }

  @Test
  void givenNonExistentOrgSilServiceIdIdWhenGetOrgSilServiceByIdDecryptedThenReturnNull() {
    Long orgSilServiceId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolderMock.getOrganizationSilServiceApi(accessToken)).thenReturn(organizationSilServiceApiMock);
    when(organizationSilServiceApiMock.getOrgSilService(orgSilServiceId)).thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    OrgSilServiceDTO result = orgSilServiceSearchClient.getOrgSilServiceByIdDecrypted(orgSilServiceId, accessToken);

    assertNull(result);
  }

}
