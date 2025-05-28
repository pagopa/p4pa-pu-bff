package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.InstallmentRegistrySearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentRegistryServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
  @Mock
  private InstallmentRegistrySearchClient installmentRegistrySearchClientMock;

  private InstallmentRegistryService service;

  @BeforeEach
  void setUp() {
    service = new InstallmentRegistryServiceImpl(installmentRegistrySearchClientMock);
  }

  @Test
  void whenGetInstallmentRegistriesThenInvokeClient() {
    Long debtPositionId = 1L;
    CollectionModelInstallmentRegistry expectedResult = new CollectionModelInstallmentRegistry();

    when(installmentRegistrySearchClientMock.getInstallmentRegistries(debtPositionId, ACCESS_TOKEN))
      .thenReturn(expectedResult);

    CollectionModelInstallmentRegistry result = service.getInstallmentRegistries(debtPositionId, ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
