package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.controller.generated.DebtPositionRegistrySearchControllerApi;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
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
class DebtPositionRegistrySearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private RegistriesApisHolder registriesApisHolderMock;
  @Mock
  private DebtPositionRegistrySearchControllerApi debtPositionRegistrySearchControllerApiMock;

  private DebtPositionRegistrySearchClient debtPositionRegistrySearchClient;

  @BeforeEach
  void setUp() {
    debtPositionRegistrySearchClient = new DebtPositionRegistrySearchClient(registriesApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(registriesApisHolderMock,debtPositionRegistrySearchControllerApiMock);
  }

  @Test
  void whenFindDebtPositionRegistriesThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long debtPositionId = 1L;
    CollectionModelDebtPositionRegistry expectedResponse = podamFactory.manufacturePojo(CollectionModelDebtPositionRegistry.class);

    when(registriesApisHolderMock.getDebtPositionRegistrySearchControllerApi(accessToken))
      .thenReturn(debtPositionRegistrySearchControllerApiMock);
    when(debtPositionRegistrySearchControllerApiMock.crudDebtPositionRegistriesFindAllByDebtPositionId(debtPositionId)).thenReturn(
      expectedResponse);

    CollectionModelDebtPositionRegistry response = debtPositionRegistrySearchClient.findDebtPositionRegistries(debtPositionId,accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse,response);
  }
}
