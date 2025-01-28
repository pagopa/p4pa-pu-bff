package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeDTOMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeServiceImplTest {

  @Mock
  private DebtPositionTypeClient debtPositionTypeClientMock;

  @Mock
  private DebtPositionTypeDTOMapper debtPositionTypeDTOMapperMock;

  private DebtPositionTypeServiceImpl debtPositionTypeService;

  private DebtPositionType debtPositionType;
  private DebtPositionTypeDTO debtPositionTypeDTO;

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

    debtPositionTypeDTO = DebtPositionTypeDTO.builder()
      .debtPositionTypeId(123L)
      .brokerId(456L)
      .code("CODE001")
      .description("Test Description")
      .orgType("OrgType001")
      .macroArea("MacroArea001")
      .serviceType("ServiceType001")
      .collectingReason("Collecting Reason 001")
      .taxonomyCode("TaxonomyCode001")
      .flagAnonymousFiscalCode(true)
      .flagMandatoryDueDate(false)
      .flagNotifyIo(true)
      .ioTemplateMessage("Test IO Template Message")
      .build();

    debtPositionTypeService = new DebtPositionTypeServiceImpl(debtPositionTypeClientMock, debtPositionTypeDTOMapperMock);
  }

  @Test
  void testGetDebtPositionTypeById() {
    Mockito.when(debtPositionTypeClientMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(debtPositionType);
    Mockito.when(debtPositionTypeDTOMapperMock.mapToDebtPositionTypeDTO(any(DebtPositionType.class)))
      .thenReturn(debtPositionTypeDTO);

    DebtPositionTypeDTO result = debtPositionTypeService.getDebtPositionTypeById(accessToken, 123L);

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
    Mockito.when(debtPositionTypeClientMock.getDebtPositionTypeById(anyLong(), any()))
      .thenReturn(null);

    DebtPositionTypeDTO result = debtPositionTypeService.getDebtPositionTypeById(accessToken, 123L);

    assertNull(result);
  }

}

