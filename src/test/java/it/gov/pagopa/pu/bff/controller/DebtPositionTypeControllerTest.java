package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeControllerTest {

  @Mock
  private DebtPositionTypeService debtPositionTypeServiceMock;

  @InjectMocks
  private DebtPositionTypeController debtPositionTypeController;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

    debtPositionTypeDTO = new DebtPositionTypeDTO();
    debtPositionTypeDTO.setDebtPositionTypeId(123L);
    debtPositionTypeDTO.setBrokerId(456L);
    debtPositionTypeDTO.setCode("CODE001");
    debtPositionTypeDTO.setDescription("Test Debt Position Type");
    debtPositionTypeDTO.setOrgType("OrgType001");
    debtPositionTypeDTO.setMacroArea("MacroArea001");
    debtPositionTypeDTO.setServiceType("ServiceType001");
    debtPositionTypeDTO.setCollectingReason("Collecting Reason 001");
    debtPositionTypeDTO.setTaxonomyCode("TaxonomyCode001");
    debtPositionTypeDTO.setFlagAnonymousFiscalCode(true);
    debtPositionTypeDTO.setFlagMandatoryDueDate(false);
    debtPositionTypeDTO.setFlagNotifyIo(true);
    debtPositionTypeDTO.setIoTemplateMessage("Test IO Template Message");
  }


  @Test
  void testGetDebtPositionType() {
    when(debtPositionTypeServiceMock.getDebtPositionTypeById(any(), anyLong())).thenReturn(debtPositionTypeDTO);

    ResponseEntity<DebtPositionTypeDTO> response = debtPositionTypeController.getDebtPositionType("123");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(123L, response.getBody().getDebtPositionTypeId());
    assertEquals(456L, response.getBody().getBrokerId());
    assertEquals("CODE001", response.getBody().getCode());
    assertEquals("Test Debt Position Type", response.getBody().getDescription());
    assertEquals("OrgType001", response.getBody().getOrgType());
    assertEquals("MacroArea001", response.getBody().getMacroArea());
    assertEquals("ServiceType001", response.getBody().getServiceType());
    assertEquals("Collecting Reason 001", response.getBody().getCollectingReason());
    assertEquals("TaxonomyCode001", response.getBody().getTaxonomyCode());
    assertEquals(true, response.getBody().getFlagAnonymousFiscalCode());
    assertEquals(false, response.getBody().getFlagMandatoryDueDate());
    assertEquals(true, response.getBody().getFlagNotifyIo());
    assertEquals("Test IO Template Message", response.getBody().getIoTemplateMessage());

    verify(debtPositionTypeServiceMock, times(1)).getDebtPositionTypeById(any(), eq(123L));
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeWithCountThenOk() {
    long organizationId = 1L;
    PagedDebtPositionTypeWithCount expectedResult = new PagedDebtPositionTypeWithCount();
    expectedResult.setContent(List.of(DebtPositionTypeWithCount.builder()
      .debtPositionTypeId(1L)
      .code("code")
      .description("description")
      .updateDate(OffsetDateTime.now())
      .activeOrganizations(10)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(0L);
    expectedResult.setNumber(0L);

    Mockito.when(debtPositionTypeServiceMock.getDebtPositionTypeWithCount(Mockito.eq(organizationId),
        Mockito.argThat(p->p.getPageNumber()==0 && p.getPageSize()==10 && p.getSort().isUnsorted()),
        Mockito.any(), Mockito.anyString()))
      .thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeWithCount> response = debtPositionTypeController.getDebtPositionTypeWithCount(organizationId,
      PageRequest.of(0,10));

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
  }
}

