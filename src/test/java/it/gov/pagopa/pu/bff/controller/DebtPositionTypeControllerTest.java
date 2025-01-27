package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  private DebtPositionTypeDTO debtPositionTypeDTO;

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

}
