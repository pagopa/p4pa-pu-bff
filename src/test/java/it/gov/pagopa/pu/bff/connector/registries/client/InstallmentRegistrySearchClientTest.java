package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.client.generated.InstallmentRegistrySearchControllerApi;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;
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
class InstallmentRegistrySearchClientTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private RegistriesApisHolder registriesApisHolderMock;
  @Mock
  private InstallmentRegistrySearchControllerApi installmentRegistrySearchControllerApiMock;

  private InstallmentRegistrySearchClient installmentRegistrySearchClient;

  @BeforeEach
  void setUp() {
    installmentRegistrySearchClient = new InstallmentRegistrySearchClient(registriesApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      registriesApisHolderMock,
      installmentRegistrySearchControllerApiMock
    );
  }

  @Test
  void whenGetInstallmentRegistriesThenInvokeWithAccessToken() {
    String accessToken = "TOKEN";
    Long debtPositionId = 1L;
    String nav = "nav";
    CollectionModelInstallmentRegistry expectedResponse = podamFactory.manufacturePojo(CollectionModelInstallmentRegistry.class);

    when(registriesApisHolderMock.getInstallmentRegistrySearchControllerApi(accessToken))
      .thenReturn(installmentRegistrySearchControllerApiMock);
    when(installmentRegistrySearchControllerApiMock.crudInstallmentRegistriesFindAllByDebtPositionIdAndNav(debtPositionId, nav))
      .thenReturn(expectedResponse);

    CollectionModelInstallmentRegistry response = installmentRegistrySearchClient.getInstallmentRegistries(debtPositionId, nav, accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse, response);
  }
}
