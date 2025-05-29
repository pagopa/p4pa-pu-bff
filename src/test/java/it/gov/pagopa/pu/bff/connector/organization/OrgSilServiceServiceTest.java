package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.OrgSilServiceSearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelOrgSilService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
