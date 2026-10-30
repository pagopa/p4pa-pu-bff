package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.client.generated.PdndClientApi;
import it.gov.pagopa.pu.organization.dto.generated.PagedPdndClientView;
import it.gov.pagopa.pu.organization.dto.generated.PdndClient;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdndClientClientTest {
  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private static final String CLIENT_ID = "CLIENT_001";
  private static final Long ORGANIZATION_ID = 123L;
  private static final String SUB_UNIT_CODE = "SUB_UNIT_001";
  private static final String SUB_UNIT_NAME = "SUB_UNIT_001_NAME";
  private static final String ACCESS_TOKEN = "accessToken";

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;

  @Mock
  private PdndClientApi pdndClientApiMock;

  private PdndClientClient pdndClientClient;

  @BeforeEach
  void setUp() {
    pdndClientClient = new PdndClientClient(organizationApisHolderMock);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(
      organizationApisHolderMock,
      pdndClientApiMock
    );
  }

  @Test
  void givenOrganizationIdAndSubUnitCodeWhenGetPdndClientsThenReturnClients() {
    List<PdndClientNoSecretDTO> expectedResult = List.of(new PdndClientNoSecretDTO());

    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    when(pdndClientApiMock.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE))
      .thenReturn(expectedResult);

    List<PdndClientNoSecretDTO> result = pdndClientClient.getPdndClientsByOrganizationIdAndSubUnitCode(ORGANIZATION_ID, SUB_UNIT_CODE, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenOrganizationIdAndClientIdWhenGetPdndClientThenReturnClient() {
    PdndClientNoSecretDTO expectedResult = new PdndClientNoSecretDTO();

    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    when(pdndClientApiMock.getPdndClient(ORGANIZATION_ID, CLIENT_ID))
      .thenReturn(expectedResult);

    PdndClientNoSecretDTO result = pdndClientClient.getPdndClient(ORGANIZATION_ID, CLIENT_ID, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenPdndClientDTOWhenSavePdndClientThenReturnSavedPdndClient() {
    PdndClientDTO pdndClientDTO = new PdndClientDTO();
    PdndClient expectedResult = new PdndClient();

    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    when(pdndClientApiMock.savePdndClient(pdndClientDTO))
      .thenReturn(expectedResult);

    PdndClient result = pdndClientClient.savePdndClient(pdndClientDTO, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }

  @Test
  void givenOrganizationIdAndClientIdWhenDeletePdndClientThenOk() {
    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    pdndClientClient.deletePdndClient(ORGANIZATION_ID, CLIENT_ID, ACCESS_TOKEN);

    verify(pdndClientApiMock).deletePdndClient(ORGANIZATION_ID, CLIENT_ID);
  }

  @Test
  void whenGetOrgSubUnitsPdndClientsThenOk() {
    Pageable pageable = Pageable.ofSize(5);
    PagedPdndClientView expectedResult = podamFactory.manufacturePojo(PagedPdndClientView.class);

    when(organizationApisHolderMock.getPdndClientApi(ACCESS_TOKEN))
      .thenReturn(pdndClientApiMock);

    when(pdndClientApiMock.getOrgSubUnitsPdndClients(ORGANIZATION_ID, SUB_UNIT_CODE, SUB_UNIT_NAME,
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedPdndClientView result = pdndClientClient.getOrgSubUnitsPdndClients(ORGANIZATION_ID, SUB_UNIT_CODE, SUB_UNIT_NAME, pageable, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
