package it.gov.pagopa.pu.bff.connector.debt_position;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView.DebtPositionOriginEnum;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

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
  void whenGetDebtPositionViewsThenInvokeClient() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    String accessToken = "ACCESSTOKEN";
    String operatorExternalUserId = "operatorExternalUserId";
    List<String> debtPositionOrigins = List.of(DebtPositionOriginEnum.ORDINARY.toString(),DebtPositionOriginEnum.ORDINARY_SIL.toString(), DebtPositionOriginEnum.SPONTANEOUS.toString());
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

    when(clientMock.getDebtPosition(debtPositionId,accessToken))
      .thenReturn(expectedResult);

    DebtPositionDTO result = service.getDebtPosition(debtPositionId, accessToken);

    assertSame(expectedResult, result);
  }
}
