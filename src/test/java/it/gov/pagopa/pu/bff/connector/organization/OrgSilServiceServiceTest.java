package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSilServiceSearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSilServiceView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSilServiceServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final String accessToken = "accessToken";

  @Mock
  private OrgSilServiceSearchClient orgSilServiceSearchClientMock;

  private OrgSilServiceService service;

  @BeforeEach
  void setUp() {
    service = new OrgSilServiceServiceImpl(orgSilServiceSearchClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(orgSilServiceSearchClientMock);
  }

  @Test
  void whenGetOrgSilServicesThenInvokeClient() {
    Long organizationId=1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    CollectionModelOrgSilService expectedResult = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);
    when(orgSilServiceSearchClientMock.getOrgSilServices(organizationId,serviceType,accessToken))
      .thenReturn(expectedResult);

    CollectionModelOrgSilService result = service.getOrgSilServices(organizationId,serviceType, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetOrgSilServiceByIdThenInvokeClient() {
    Long orgSilServiceId = 1L;
    OrgSilService expectedResult = podamFactory.manufacturePojo(OrgSilService.class);
    when(orgSilServiceSearchClientMock.getOrgSilServiceById(orgSilServiceId, accessToken))
      .thenReturn(expectedResult);

    OrgSilService result = service.getOrgSilServiceById(orgSilServiceId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetOrgSilServicesByFiltersThenInvokeClient() {
    Long organizationId = 1L;
    String applicationName = "myApp";
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    boolean flagLegacy = true;
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelOrgSilServiceView expectedResult =
      podamFactory.manufacturePojo(PagedModelOrgSilServiceView.class);

    when(orgSilServiceSearchClientMock.getOrgSilServicesByFilters(organizationId, applicationName, serviceType, flagLegacy, pageable, accessToken))
      .thenReturn(expectedResult);

    PagedModelOrgSilServiceView result = service.getOrgSilServicesByFilters(
        organizationId, applicationName, serviceType, flagLegacy, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
