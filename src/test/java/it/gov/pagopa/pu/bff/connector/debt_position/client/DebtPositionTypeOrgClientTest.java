package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
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
class DebtPositionTypeOrgClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;

  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;

  private DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgClient = new DebtPositionTypeOrgClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgSearchControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrg expectedResult = new CollectionModelDebtPositionTypeOrg();

    long organizationId = 1L;
    String operatorExternalUserId = "operator123";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);

    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(
      String.valueOf(organizationId), operatorExternalUserId))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

}
