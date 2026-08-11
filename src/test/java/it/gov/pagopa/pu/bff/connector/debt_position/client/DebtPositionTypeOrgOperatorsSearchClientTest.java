package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.client.generated.DebtPositionTypeOrgOperatorsSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsSearchClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgOperatorsSearchControllerApi debtPositionTypeOrgOperatorsSearchControllerApiMock;

  private DebtPositionTypeOrgOperatorsSearchClient debtPositionTypeOrgOperatorsSearchClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgOperatorsSearchClient = new DebtPositionTypeOrgOperatorsSearchClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock);
  }


  @Test
  void whenGetDebtPositionTypeOrgOperatorsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrgOperators expectedResult = new CollectionModelDebtPositionTypeOrgOperators();

    long debtPositionTypeOrgId = 1L;

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsSearchControllerApiMock);

    when(debtPositionTypeOrgOperatorsSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgId(debtPositionTypeOrgId))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgOperators result = debtPositionTypeOrgOperatorsSearchClient.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExistingOperatorWhenFindByDebtPositionTypeOrgIdAndOperatorExternalUserIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrgOperators expectedResult = new DebtPositionTypeOrgOperators();
    long debtPositionTypeOrgId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsSearchControllerApiMock);
    when(debtPositionTypeOrgOperatorsSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId,operatorExternalUserId))
      .thenReturn(expectedResult);

    DebtPositionTypeOrgOperators result = debtPositionTypeOrgOperatorsSearchClient.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNonExistingOperatorWhenFindByDebtPositionTypeOrgIdAndOperatorExternalUserIdThenNull() {
    String accessToken = "ACCESSTOKEN";
    long debtPositionTypeOrgId = 1L;
    String operatorExternalUserId = "operatorExternalUserId";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsSearchControllerApiMock);
    when(debtPositionTypeOrgOperatorsSearchControllerApiMock.crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId,operatorExternalUserId))
      .thenReturn(null);

    DebtPositionTypeOrgOperators result = debtPositionTypeOrgOperatorsSearchClient.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId, operatorExternalUserId, accessToken);

    assertNull(result);
  }
}
