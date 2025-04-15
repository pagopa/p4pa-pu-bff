package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DebtPositionSearchClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionSearchControllerApi debtPositionSearchControllerApiMock;
  private DebtPositionSearchClient debtPositionSearchClient;

  @BeforeEach
  void setUp() {
    debtPositionSearchClient = new DebtPositionSearchClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock);
  }

  @Test
  void whenGetDebtPositionByDebtPositionTypeOrgIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPosition expectedResult = new CollectionModelDebtPosition();

    long debtPositionTypeOrgId = 1L;

    when(debtPositionApisHolderMock.getDebtPositionSearchControllerApi(accessToken))
      .thenReturn(debtPositionSearchControllerApiMock);

    when(debtPositionSearchControllerApiMock.crudDebtPositionsFindByDebtPositionTypeOrgId(
      debtPositionTypeOrgId))
      .thenReturn(expectedResult);

    CollectionModelDebtPosition result = debtPositionSearchClient.getDebtPositionByDebtPositionTypeOrgId(debtPositionTypeOrgId, accessToken);

    assertSame(expectedResult, result);
  }
}
