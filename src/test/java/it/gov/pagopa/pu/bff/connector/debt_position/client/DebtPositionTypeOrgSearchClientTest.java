package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgSearchClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;
  private DebtPositionTypeOrgSearchClient debtPositionTypeOrgSearchClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgSearchClient = new DebtPositionTypeOrgSearchClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgByDebtPositionTypeIdThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPositionTypeOrg expectedResult = new PagedModelDebtPositionTypeOrg();

    long debtPositionTypeId = 1L;

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);

    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByDebtPositionTypeId(
      debtPositionTypeId, 1, 1, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelDebtPositionTypeOrg result = debtPositionTypeOrgSearchClient.getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId,
      PageRequest.of(1, 1), accessToken);

    assertSame(expectedResult, result);
  }
}
