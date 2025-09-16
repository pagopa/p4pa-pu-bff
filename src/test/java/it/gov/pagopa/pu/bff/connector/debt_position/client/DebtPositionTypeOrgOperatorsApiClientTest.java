package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgOperatorsApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsApiClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgOperatorsApi debtPositionTypeOrgOperatorsApiMock;

  private DebtPositionTypeOrgOperatorsApiClient debtPositionTypeOrgOperatorsApiClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgOperatorsApiClient = new DebtPositionTypeOrgOperatorsApiClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgOperatorsApiMock);
  }

  @Test
  void whenDeleteOperatorsThenInvokeApiAndReturnDeletedCount() {
    String accessToken = "ACCESSTOKEN";
    Long debtPositionTypeOrgId = 123L;
    Set<String> externalOperatorUserIds = Set.of("user1", "user2");
    int expectedDeleted = 2;

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgOperatorsApi(accessToken))
      .thenReturn(debtPositionTypeOrgOperatorsApiMock);
    when(debtPositionTypeOrgOperatorsApiMock.deleteOperators(debtPositionTypeOrgId, externalOperatorUserIds))
      .thenReturn(expectedDeleted);

    int result = debtPositionTypeOrgOperatorsApiClient
      .deleteOperators(debtPositionTypeOrgId, externalOperatorUserIds, accessToken);

    assertEquals(expectedDeleted, result);
  }
}
