package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgBalanceCostSearchControllerApi;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostClientTest {
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgBalanceCostSearchControllerApi debtPositionTypeOrgBalanceCostSearchControllerApiMock;

  private DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgBalanceCostClient = new DebtPositionTypeOrgBalanceCostClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgBalanceCostSearchControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearThenInvokeWithAccessToken() {
    long dptoId = 1L;
    String opYear = "2025";
    String accessToken = "accessToken";

    CollectionModelDebtPositionTypeOrgBalanceCost expectedResult = new CollectionModelDebtPositionTypeOrgBalanceCost();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCostSearchControllerApiMock);
    when(debtPositionTypeOrgBalanceCostSearchControllerApiMock.crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYear(dptoId, opYear))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndTypeThenInvokeWithAccessToken() {
    long dptoId = 1L;
    String opYear = "2025";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.DELAY_COST;
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost expectedResult = new DebtPositionTypeOrgBalanceCost();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCostSearchControllerApiMock);
    when(debtPositionTypeOrgBalanceCostSearchControllerApiMock.crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYearAndType(dptoId, opYear, type))
      .thenReturn(expectedResult);

    DebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoDebtPositionTypeOrgBalanceCostWhenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndTypeThenReturnNull() {
    long dptoId = 1L;
    String opYear = "2025";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.DELAY_COST;
    String accessToken = "accessToken";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCostSearchControllerApiMock);
    when(debtPositionTypeOrgBalanceCostSearchControllerApiMock.crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYearAndType(dptoId, opYear, type))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    DebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken);

    assertNull(result);
  }
}
