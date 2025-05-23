package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.DebtPositionRegistrySearchClient;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionRegistryServiceTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
  @Mock
  private DebtPositionRegistrySearchClient debtPositionRegistrySearchClientMock;

  private DebtPositionRegistryService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionRegistryServiceImpl(debtPositionRegistrySearchClientMock);
  }

  @Test
  void whenFindDebtPositionRegistriesThenInvokeClient() {
    Long debtPositionId = 1L;
    CollectionModelDebtPositionRegistry expectedResult = new CollectionModelDebtPositionRegistry();

    when(debtPositionRegistrySearchClientMock.findDebtPositionRegistries(debtPositionId,ACCESS_TOKEN))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionRegistry result = service.findDebtPositionRegistries(debtPositionId,ACCESS_TOKEN);

    assertSame(expectedResult, result);
  }
}
