package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.client.generated.PdndServiceSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.CollectionModelPdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdndServiceSearchClientTest {
  private static final String ACCESS_TOKEN = "accessToken";
  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private PdndServiceSearchControllerApi pdndServiceSearchControllerApiMock;

  @InjectMocks
  private PdndServiceSearchClient pdndServiceSearchClient;


  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(
      organizationApisHolderMock,
      pdndServiceSearchControllerApiMock
    );
  }

  @Test
  void whenFindByOrganizationIdAndClientIdThenOk() {
    Long organizationId = 1L;
    String clientId = "clientId";
    PdndServiceType serviceType = PdndServiceType.SEND;
    CollectionModelPdndService collectionModelPdndService = podamFactory.manufacturePojo(CollectionModelPdndService.class);

    when(organizationApisHolderMock.getPdndServiceSearchControllerApi(ACCESS_TOKEN))
      .thenReturn(pdndServiceSearchControllerApiMock);

    when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByOrganizationIdAndClientId(organizationId, clientId, serviceType))
      .thenReturn(collectionModelPdndService);

    List<PdndService> result = pdndServiceSearchClient.findByOrganizationIdAndClientId(organizationId, clientId, serviceType, ACCESS_TOKEN);

    assertSame(collectionModelPdndService.getEmbedded().getPdndServices(), result);
  }

  @Test
  void givenNoPdndServicesWhenFindByOrganizationIdAndClientIdThenEmptyList() {
    Long organizationId = 1L;
    String clientId = "clientId";
    PdndServiceType serviceType = PdndServiceType.SEND;
    CollectionModelPdndService collectionModelPdndService = podamFactory.manufacturePojo(CollectionModelPdndService.class);
    collectionModelPdndService.getEmbedded().setPdndServices(null);

    when(organizationApisHolderMock.getPdndServiceSearchControllerApi(ACCESS_TOKEN))
      .thenReturn(pdndServiceSearchControllerApiMock);

    when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByOrganizationIdAndClientId(organizationId, clientId, serviceType))
      .thenReturn(collectionModelPdndService);

    List<PdndService> result = pdndServiceSearchClient.findByOrganizationIdAndClientId(organizationId, clientId, serviceType, ACCESS_TOKEN);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoEmbeddedWhenFindByOrganizationIdAndClientIdThenEmptyList() {
    Long organizationId = 1L;
    String clientId = "clientId";
    PdndServiceType serviceType = PdndServiceType.SEND;
    CollectionModelPdndService collectionModelPdndService = podamFactory.manufacturePojo(CollectionModelPdndService.class);
    collectionModelPdndService.setEmbedded(null);

    when(organizationApisHolderMock.getPdndServiceSearchControllerApi(ACCESS_TOKEN))
      .thenReturn(pdndServiceSearchControllerApiMock);

    when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByOrganizationIdAndClientId(organizationId, clientId, serviceType))
      .thenReturn(collectionModelPdndService);

    List<PdndService> result = pdndServiceSearchClient.findByOrganizationIdAndClientId(organizationId, clientId, serviceType, ACCESS_TOKEN);

    assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNoCollectionModelPdndServiceEmbeddedWhenFindByOrganizationIdAndClientIdThenEmptyList() {
    Long organizationId = 1L;
    String clientId = "clientId";
    PdndServiceType serviceType = PdndServiceType.SEND;

    when(organizationApisHolderMock.getPdndServiceSearchControllerApi(ACCESS_TOKEN))
      .thenReturn(pdndServiceSearchControllerApiMock);

    when(pdndServiceSearchControllerApiMock.crudPdndServicesFindByOrganizationIdAndClientId(organizationId, clientId, serviceType))
      .thenReturn(null);

    List<PdndService> result = pdndServiceSearchClient.findByOrganizationIdAndClientId(organizationId, clientId, serviceType, ACCESS_TOKEN);

    assertTrue(CollectionUtils.isEmpty(result));
  }
}
