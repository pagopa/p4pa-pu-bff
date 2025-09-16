package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsApiClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsDptoCountViewClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsServiceImplTest {

  @Mock
  private DebtPositionTypeOrgOperatorsSearchClient debtPositionTypeOrgOperatorsSearchClientMock;
  @Mock
  private DebtPositionTypeOrgOperatorsDptoCountViewClient debtPositionTypeOrgOperatorsDptoCountViewClientMock;
  @Mock
  private DebtPositionTypeOrgOperatorsApiClient debtPositionTypeOrgOperatorsApiClientMock;

  private DebtPositionTypeOrgOperatorsService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeOrgOperatorsServiceImpl(debtPositionTypeOrgOperatorsSearchClientMock,debtPositionTypeOrgOperatorsDptoCountViewClientMock, debtPositionTypeOrgOperatorsApiClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrgOperators expectedResult = new CollectionModelDebtPositionTypeOrgOperators();

    when(debtPositionTypeOrgOperatorsSearchClientMock.getDebtPositionTypeOrgOperators(Mockito.same(debtPositionTypeOrgId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgOperators result = service.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindByDebtPositionTypeOrgIdAndOperatorExternalUserIdThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String operatorExternalUsreId = "operatorExternalUsreId";
    String accessToken = "ACCESSTOKEN";
    DebtPositionTypeOrgOperators expectedResult = new DebtPositionTypeOrgOperators();

    when(debtPositionTypeOrgOperatorsSearchClientMock.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(
      debtPositionTypeOrgId,operatorExternalUsreId,accessToken))
      .thenReturn(expectedResult);

    DebtPositionTypeOrgOperators result = service.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId, operatorExternalUsreId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenFindByOrganizationIdAndOperatorExternalUserIdsThenInvokeClient() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Set<String> operatorExternalUserIds = Set.of("operatorExternalUserId");
    List<DebtPositionTypeOrgOperatorsDptoCountView> expectedResult = List.of(new DebtPositionTypeOrgOperatorsDptoCountView());

    when(debtPositionTypeOrgOperatorsDptoCountViewClientMock.findByOrganizationIdAndOperatorExternalUserIds(
      organizationId,operatorExternalUserIds,accessToken))
      .thenReturn(expectedResult);

    List<DebtPositionTypeOrgOperatorsDptoCountView> result = service.findByOrganizationIdAndOperatorExternalUserIds(organizationId, operatorExternalUserIds, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenDeleteOperatorsThenInvokeApiClientAndReturnDeletedCount() {
    Long debtPositionTypeOrgId = 1L;
    Set<String> externalOperatorUserIds = Set.of("user1", "user2");
    String accessToken = "ACCESSTOKEN";
    int expectedDeleted = 2;

    when(debtPositionTypeOrgOperatorsApiClientMock.deleteOperators(
      debtPositionTypeOrgId,
      externalOperatorUserIds,
      accessToken))
      .thenReturn(expectedDeleted);

    int result = service.deleteOperators(
      debtPositionTypeOrgId,
      externalOperatorUserIds,
      accessToken
    );

    assertEquals(expectedDeleted, result);
  }
}
