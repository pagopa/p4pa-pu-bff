package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgBalanceCostClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostServiceImplTest {
  @Mock
  private DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClientMock;

  private DebtPositionTypeOrgBalanceCostService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeOrgBalanceCostServiceImpl(debtPositionTypeOrgBalanceCostClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgBalanceCostClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearThenInvokeClient() {
    long dptoId = 1L;
    String opYear = "2026";
    String accessToken = "accessToken";

    CollectionModelDebtPositionTypeOrgBalanceCost expectedResult = new CollectionModelDebtPositionTypeOrgBalanceCost();

    when(debtPositionTypeOrgBalanceCostClientMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgBalanceCost result = service.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndTypeThenInvokeClient() {
    long dptoId = 1L;
    String opYear = "2026";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.DELAY_COST;
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost expectedResult = new DebtPositionTypeOrgBalanceCost();

    when(debtPositionTypeOrgBalanceCostClientMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken))
      .thenReturn(expectedResult);

    DebtPositionTypeOrgBalanceCost result = service.getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken);

    assertSame(expectedResult, result);
  }
}
