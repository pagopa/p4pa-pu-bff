package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionServiceTest {
  @Mock
  private DebtPositionClient clientMock;

  private DebtPositionService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionServiceImpl(clientMock);
  }

  @Test
  void whenCreateDebtPositionThenInvokeClient() {
    DebtPositionDTO debtPositionDTO = new DebtPositionDTO();
    Boolean massive = true;
    String accessToken = "ACCESSTOKEN";
    DebtPositionDTO expectedResult = new DebtPositionDTO();

    when(clientMock.createDebtPosition(Mockito.same(debtPositionDTO), Mockito.same(massive), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    DebtPositionDTO result = service.createDebtPosition(debtPositionDTO, massive, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionViewsThenInvokeClient() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    String accessToken = "ACCESSTOKEN";
    String operatorExternalUserId = "operatorExternalUserId";
    List<String> debtPositionOrigins = List.of(DebtPositionOrigin.ORDINARY.toString(), DebtPositionOrigin.ORDINARY_SIL.toString(), DebtPositionOrigin.SPONTANEOUS.toString());
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelDebtPositionView expectedResult = new PagedModelDebtPositionView();

    when(clientMock.getDebtPositionViews(Mockito.same(filtersDTO), Mockito.same(debtPositionOrigins), Mockito.same(operatorExternalUserId), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelDebtPositionView result = service.getDebtPositionViews(filtersDTO, debtPositionOrigins, operatorExternalUserId, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetDebtPositionThenInvokeClient() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";
    DebtPositionDTO expectedResult = new DebtPositionDTO();

    when(clientMock.getDebtPosition(debtPositionId, accessToken))
      .thenReturn(expectedResult);

    DebtPositionDTO result = service.getDebtPosition(debtPositionId, accessToken);

    assertSame(expectedResult, result);
  }
}
