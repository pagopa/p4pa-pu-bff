package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeWithCountMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeRetrieverServiceImplTest {

  @Mock
  private DebtPositionTypeService debtPositionTypeServiceMock;

  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private DebtPositionTypeWithCountMapper debtPositionTypeWithCountMapperMock;

  private DebtPositionTypeRetrieverServiceImpl debtPositionTypeService;

  private DebtPositionType debtPositionType;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionType = new DebtPositionType();
    debtPositionType.setDebtPositionTypeId(123L);
    debtPositionType.setBrokerId(456L);
    debtPositionType.setCode("CODE001");
    debtPositionType.setDescription("Test Description");
    debtPositionType.setOrgType("OrgType001");
    debtPositionType.setMacroArea("MacroArea001");
    debtPositionType.setServiceType("ServiceType001");
    debtPositionType.setCollectingReason("Collecting Reason 001");
    debtPositionType.setTaxonomyCode("TaxonomyCode001");
    debtPositionType.setFlagAnonymousFiscalCode(true);
    debtPositionType.setFlagMandatoryDueDate(false);
    debtPositionType.setFlagNotifyIo(true);
    debtPositionType.setIoTemplateMessage("Test IO Template Message");

    debtPositionTypeService = new DebtPositionTypeRetrieverServiceImpl(debtPositionTypeServiceMock, debtPositionTypeWithCountMapperMock, authorizationServiceMock);
  }

  @Test
  void testGetDebtPositionTypeById() {
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(debtPositionType);

    DebtPositionType result = debtPositionTypeService.getDebtPositionTypeById(accessToken, 123L);

    assertNotNull(result);
    assertEquals(123L, result.getDebtPositionTypeId());
    assertEquals(456L, result.getBrokerId());
    assertEquals("CODE001", result.getCode());
    assertEquals("Test Description", result.getDescription());
    assertEquals("OrgType001", result.getOrgType());
    assertEquals("MacroArea001", result.getMacroArea());
    assertEquals("ServiceType001", result.getServiceType());
    assertEquals("Collecting Reason 001", result.getCollectingReason());
    assertEquals("TaxonomyCode001", result.getTaxonomyCode());
    assertTrue(result.getFlagAnonymousFiscalCode());
    assertFalse(result.getFlagMandatoryDueDate());
    assertTrue(result.getFlagNotifyIo());
    assertEquals("Test IO Template Message", result.getIoTemplateMessage());
  }

  @Test
  void testGetDebtPositionTypeById_NullResponse() {
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(null);

    DebtPositionType result = debtPositionTypeService.getDebtPositionTypeById(accessToken, 123L);

    assertNull(result);
  }


  @Test
  void givenValidUserWhenGetDebtPositionTypeWithCountThenOK() {
    long brokerId = 1L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PagedModelDebtPositionTypeWithCount pagedModelDebtPositionTypeWithCount = new PagedModelDebtPositionTypeWithCount();
    PagedDebtPositionTypeWithCount pagedDebtPositionTypeWithCount = new PagedDebtPositionTypeWithCount();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(1L,userInfo);
    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeWithCount(brokerId, PageRequest.of(0,10),accessToken)).thenReturn(pagedModelDebtPositionTypeWithCount);
    Mockito.when(debtPositionTypeWithCountMapperMock.mapToPagedDebtPositionWithCount(pagedModelDebtPositionTypeWithCount)).thenReturn(pagedDebtPositionTypeWithCount);

    PagedDebtPositionTypeWithCount result = debtPositionTypeService.getDebtPositionTypeWithCount(
      1L, PageRequest.of(0,10),
      userInfo, accessToken);

    assertNotNull(result);
    assertSame(pagedDebtPositionTypeWithCount,result);

    Mockito.verifyNoMoreInteractions(debtPositionTypeServiceMock,debtPositionTypeWithCountMapperMock,authorizationServiceMock);
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionTypeWithCountThenAuthorizationDeniedException() {
    long brokerId = 1L;
    UserInfo userInfo = new UserInfo();
    userInfo.setBrokerId(brokerId);
    PageRequest pageRequest = PageRequest.of(0, 10);

    Mockito.doThrow(new AuthorizationDeniedException("")).when(authorizationServiceMock).validateAdminRole(1L,userInfo);

    Assertions.assertThrows(AuthorizationDeniedException.class,()->
      debtPositionTypeService.getDebtPositionTypeWithCount(
        1L, pageRequest, userInfo, accessToken));

    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(debtPositionTypeServiceMock,debtPositionTypeWithCountMapperMock);
  }
}

