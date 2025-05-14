package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgOperatorsSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgOperatorsSearchControllerApi debtPositionTypeOrgOperatorsSearchControllerApiMock;

  private DebtPositionTypeOrgOperatorsClient debtPositionTypeOrgOperatorsClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgOperatorsClient = new DebtPositionTypeOrgOperatorsClient(debtPositionApisHolderMock);
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

    CollectionModelDebtPositionTypeOrgOperators result = debtPositionTypeOrgOperatorsClient.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);

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

    DebtPositionTypeOrgOperators result = debtPositionTypeOrgOperatorsClient.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId, operatorExternalUserId, accessToken);

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

    DebtPositionTypeOrgOperators result = debtPositionTypeOrgOperatorsClient.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId, operatorExternalUserId, accessToken);

    assertNull(result);
  }
}
