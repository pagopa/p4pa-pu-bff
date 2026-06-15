package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgBalanceCostMapper;
import it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost.DebtPositionTypeOrgBalanceCostRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
  @Mock
  private AuthorizationService authorizationServiceMock;

  @InjectMocks
  private DebtPositionTypeOrgBalanceCostRetrieverServiceImpl debtPositionTypeOrgBalanceCostRetrieverService;

  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgBalanceCostServiceMock, debtPositionTypeOrgBalanceCostMapperMock, authorizationServiceMock);
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
  void givenValidParametersWhenGetDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndTypeThenOk() {
    Long orgId = 1L;
    Long dptoId = 1L;
    String opYear = "2025";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.DELAY_COST;
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost expected = new DebtPositionTypeOrgBalanceCost();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(orgId, loggedUser);
    Mockito.when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(dptoId, opYear, type, accessToken))
      .thenReturn(expected);

    DebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostRetrieverService
      .getDebtPositionTypeOrgBalanceCostByDptoIdAndYearAndType(orgId, dptoId, opYear, type, loggedUser, accessToken);

    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void givenNoDebtPositionTypeOrgBalanceCostWhenGetDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndTypeThenResourceNotFound() {
    Long organizationId = 1L;
    Long dptoId = 1L;
    String opYear = "2026";
    DebtPositionTypeOrgBalanceCostType type = DebtPositionTypeOrgBalanceCostType.DELAY_COST;
    String accessToken = "accessToken";

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(dptoId,opYear,type, accessToken))
      .thenReturn(null);

    Assertions.assertThrows(ResourceNotFoundException.class, () ->
      debtPositionTypeOrgBalanceCostRetrieverService.getDebtPositionTypeOrgBalanceCostByDptoIdAndYearAndType(organizationId,dptoId,opYear, type, loggedUser,accessToken));
  }
}
