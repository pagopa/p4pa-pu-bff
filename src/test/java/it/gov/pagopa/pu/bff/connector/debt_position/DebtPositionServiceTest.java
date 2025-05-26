package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionSearchClient;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPosition;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionServiceTest {
  @Mock
  private DebtPositionClient clientMock;
  @Mock
  private DebtPositionSearchClient debtPositionSearchClientMock;

  private DebtPositionService service;

  @BeforeEach
  void setUp() {
    service = new DebtPositionServiceImpl(clientMock,debtPositionSearchClientMock);
  }

  @Test
  void whenCreateDebtPositionThenInvokeClient() {
    DebtPositionDTO debtPositionDTO = new DebtPositionDTO();
    boolean massive = true;
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

  @Test
  void whenGetDebtPositionByDebtPositionTypeOrgIdThenInvokeClient() {
    Long debtPositionTypeOrgId = 1L;
    String accessToken = "ACCESSTOKEN";
    PagedModelDebtPosition expectedResult = new PagedModelDebtPosition();
    PageRequest pageRequest = PageRequest.of(1, 1);

    when(debtPositionSearchClientMock.getDebtPositionByDebtPositionTypeOrgId(debtPositionTypeOrgId,pageRequest,accessToken))
      .thenReturn(expectedResult);

    PagedModelDebtPosition result = service.getDebtPositionByDebtPositionTypeOrgId(debtPositionTypeOrgId,pageRequest,accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenDeleteDebtPositionByDebtPositionIdThenInvokeClient() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(clientMock.deleteDebtPosition(debtPositionId,accessToken))
      .thenReturn(false);

    boolean deletedDebtPositionPhysically = service.deleteDebtPosition(debtPositionId, accessToken);

    assertFalse(deletedDebtPositionPhysically);
  }

  @Test
  void whenValidateOperatorThenInvokeClient() {
    Long debtPositionId = 1L;
    String accessToken = "ACCESSTOKEN";
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;



    when(debtPositionSearchClientMock.validateOperator(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken))
      .thenReturn(1L);

    Long result = service.validateOperator(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken);

    assertNotNull(result);
    assertEquals(1L, result);
  }
}
