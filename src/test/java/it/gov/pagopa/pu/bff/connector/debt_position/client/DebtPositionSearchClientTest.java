package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPosition;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

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
    PagedModelDebtPosition expectedResult = new PagedModelDebtPosition();

    long debtPositionTypeOrgId = 1L;

    when(debtPositionApisHolderMock.getDebtPositionSearchControllerApi(accessToken))
      .thenReturn(debtPositionSearchControllerApiMock);

    when(debtPositionSearchControllerApiMock.crudDebtPositionsFindByDebtPositionTypeOrgId(
      debtPositionTypeOrgId,1,1, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelDebtPosition result = debtPositionSearchClient.getDebtPositionByDebtPositionTypeOrgId(debtPositionTypeOrgId,
      PageRequest.of(1,1), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenParamsWhenValidateOperatorThenInvokeWithAccessToken() {
    //given
    String accessToken = "ACCESSTOKEN";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    when(debtPositionApisHolderMock.getDebtPositionSearchControllerApi(accessToken))
      .thenReturn(debtPositionSearchControllerApiMock);

    when(debtPositionSearchControllerApiMock.crudDebtPositionsValidateOperator(debtPositionId, organizationId, loggedUser.getMappedExternalUserId())).thenReturn(1L);
    //when

    Long result = debtPositionSearchClient.validateOperator(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken);

    //then
    assertNotNull(result);
    assertEquals(1L, result);
  }
}
