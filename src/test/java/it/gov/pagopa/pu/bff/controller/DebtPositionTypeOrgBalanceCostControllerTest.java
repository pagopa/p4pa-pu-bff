package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost.DebtPositionTypeOrgBalanceCostRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostControllerTest {

  @Mock
  private DebtPositionTypeOrgBalanceCostRetrieverService debtPositionTypeOrgBalanceCostRetrieverServiceMock;

  @InjectMocks
  private DebtPositionTypeOrgBalanceCostController debtPositionTypeOrgBalanceCostController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      debtPositionTypeOrgBalanceCostRetrieverServiceMock
    );
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeOrgBalanceCostByDebtPositionTypeOrgIdAndYearAndTypeThenOk() {
    long organizationId = 1L;
    long dptoId = 1L;
    String opYear = "2026";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST;
    DebtPositionTypeOrgBalanceCost expectedResult = new DebtPositionTypeOrgBalanceCost();

    Mockito.when(debtPositionTypeOrgBalanceCostRetrieverServiceMock.getDebtPositionTypeOrgBalanceCostByDptoIdAndYearAndType(
      organizationId, dptoId, opYear, type, loggedUser, accessToken)).thenReturn(expectedResult);

    ResponseEntity<DebtPositionTypeOrgBalanceCost> response = debtPositionTypeOrgBalanceCostController.getDebtPositionTypeOrgBalanceCostByDebtPositionTypeOrgIdAndYearAndType(organizationId, dptoId, opYear, type);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }
}
