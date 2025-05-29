package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.controller.generated.OrgSilServiceSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrgSilServiceSearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrgSilServiceSearchControllerApi orgSilServiceSearchControllerApiMock;

  private OrgSilServiceSearchClient orgSilServiceSearchClient;

  @BeforeEach
  void setUp() {
    orgSilServiceSearchClient = new OrgSilServiceSearchClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(organizationApisHolderMock,orgSilServiceSearchControllerApiMock);
  }

  @Test
  void whenGetOrgSilServicesThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    OrgSilServiceType serviceType = OrgSilServiceType.ACTUALIZATION;
    CollectionModelOrgSilService expectedResponse = podamFactory.manufacturePojo(CollectionModelOrgSilService.class);

    when(organizationApisHolderMock.getOrgSilServiceSearchControllerApi(accessToken))
      .thenReturn(orgSilServiceSearchControllerApiMock);
    when(orgSilServiceSearchControllerApiMock.crudOrgSilServicesFindAllByOrganizationIdAndServiceType(organizationId,serviceType)).thenReturn(
      expectedResponse);

    CollectionModelOrgSilService response = orgSilServiceSearchClient.getOrgSilServices(organizationId,serviceType,accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse,response);
  }
}
