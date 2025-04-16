package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsServiceImplTest {

  @Mock
  private DebtPositionTypeOrgOperatorsClient debtPositionTypeOrgOperatorsClientMock;


  private DebtPositionTypeOrgOperatorsService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeOrgOperatorsServiceImpl(debtPositionTypeOrgOperatorsClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";
    CollectionModelDebtPositionTypeOrgOperators expectedResult = new CollectionModelDebtPositionTypeOrgOperators();

    when(debtPositionTypeOrgOperatorsClientMock.getDebtPositionTypeOrgOperators(Mockito.same(debtPositionTypeOrgId), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgOperators result = service.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);

    assertSame(expectedResult, result);
  }
}
