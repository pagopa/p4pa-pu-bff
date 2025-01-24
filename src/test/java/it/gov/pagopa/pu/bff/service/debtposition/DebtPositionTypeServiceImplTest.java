package it.gov.pagopa.pu.bff.service.debtposition;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.gov.pagopa.pu.bff.connector.debtposition.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PagedModelDebtPositionTypeWithCount;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeServiceImplTest {
  @Mock
  private DebtPositionClient debtPositionClientMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapperMock;
  private DebtPositionTypeService debtPositionTypeService;
  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {

    debtPositionTypeService = new DebtPositionTypeServiceImpl(
      debtPositionClientMock,
      debtPositionTypeWithCountMapperMock,
      authorizationServiceMock
    );
  }

  @Test
  void givenValidUserWhenGetDebtPositionTypeWithCountThenOK() {
    long brokerId = 1L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PagedDebtPositionTypeWithCount pagedDebtPositionTypeWithCount = new PagedDebtPositionTypeWithCount();
    List<String> sortList = List.of("sort1","sort2");

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(1L,userInfo);
    Mockito.when(debtPositionClientMock.getDebtPositionTypeWithCount(brokerId, 0, 10,
      sortList,accessToken)).thenReturn(pagedModelDebtPositionTypeWithCount);
    Mockito.when(debtPositionTypeWithCountMapperMock.mapToPagedDebtPositionWithCount(pagedModelDebtPositionTypeWithCount)).thenReturn(pagedDebtPositionTypeWithCount);

    PagedDebtPositionTypeWithCount result = debtPositionTypeService.getDebtPositionTypeWithCount(
      1L, 0, 10L, sortList, userInfo, accessToken);

    assertNotNull(result);
    assertSame(pagedDebtPositionTypeWithCount,result);

    Mockito.verifyNoMoreInteractions(debtPositionClientMock,debtPositionTypeWithCountMapperMock,authorizationServiceMock);
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeWithCountThenAuthorizationDeniedException() {
    long brokerId = 1L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    List<String> sortList = List.of("sort1","sort2");

    Mockito.doThrow(new AuthorizationDeniedException("")).when(authorizationServiceMock).validateAdminRole(1L,userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class,()->
      debtPositionTypeService.getDebtPositionTypeWithCount(
      1L, 0, 10L, sortList, userInfo, accessToken));

    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionClientMock,debtPositionTypeWithCountMapperMock);
  }
}
