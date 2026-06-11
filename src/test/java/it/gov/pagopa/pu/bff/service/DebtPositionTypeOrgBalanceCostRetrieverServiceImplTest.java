package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgBalanceCostMapper;
import it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost.DebtPositionTypeOrgBalanceCostRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgBalanceCostEmbedded;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostRetrieverServiceImplTest {
  @Mock
  private DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostServiceMock;
  @Mock
  private DebtPositionTypeOrgBalanceCostMapper debtPositionTypeOrgBalanceCostMapperMock;

  @InjectMocks
  private DebtPositionTypeOrgBalanceCostRetrieverServiceImpl debtPositionTypeOrgBalanceCostRetrieverService;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgBalanceCostServiceMock, debtPositionTypeOrgBalanceCostMapperMock);
  }

  @Test
  void givenValidParametersWhenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearThenOk() {
    Long dptoId = 1L;
    String opYear = "2026";
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost sourceDto = new DebtPositionTypeOrgBalanceCost();
    DebtPositionTypeOrgBalanceCostDTO mappedDto = new DebtPositionTypeOrgBalanceCostDTO();

    CollectionModelDebtPositionTypeOrgBalanceCost collectionModelDebtPositionTypeOrgBalanceCost = new CollectionModelDebtPositionTypeOrgBalanceCost();
    PagedModelDebtPositionTypeOrgBalanceCostEmbedded pagedModelDebtPositionTypeOrgBalanceCostEmbedded = new PagedModelDebtPositionTypeOrgBalanceCostEmbedded();
    pagedModelDebtPositionTypeOrgBalanceCostEmbedded.setDebtPositionTypeOrgBalanceCosts(List.of(sourceDto));
    collectionModelDebtPositionTypeOrgBalanceCost.setEmbedded(pagedModelDebtPositionTypeOrgBalanceCostEmbedded);

    Mockito.when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken))
      .thenReturn(collectionModelDebtPositionTypeOrgBalanceCost);
    Mockito.when(debtPositionTypeOrgBalanceCostMapperMock.map(sourceDto))
      .thenReturn(mappedDto);

    List<DebtPositionTypeOrgBalanceCostDTO> result = debtPositionTypeOrgBalanceCostRetrieverService
      .getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(mappedDto, result.getFirst());
  }

  @Test
  void givenValidParametersWhenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndTypeThenOk() {
    Long dptoId = 1L;
    String opYear = "2025";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.DELAY_COST;
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost expected = new DebtPositionTypeOrgBalanceCost();

    Mockito.when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken))
      .thenReturn(expected);

    DebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostRetrieverService
      .getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken);

    assertNotNull(result);
    assertEquals(expected, result);
  }
}
