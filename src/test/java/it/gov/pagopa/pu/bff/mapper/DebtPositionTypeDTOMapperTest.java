package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeDTOMapperTest {

  private final DebtPositionTypeDTOMapper mapper = new DebtPositionTypeDTOMapper();

  @Test
  void testMapToDebtPositionTypeDTO() {
    DebtPositionType debtPositionType = getDebtPositionType();

    DebtPositionTypeDTO dto = mapper.mapToDebtPositionTypeDTO(debtPositionType);

    TestUtils.checkNotNullFields(dto);

    assertEquals(123L, dto.getDebtPositionTypeId());
    assertEquals(456L, dto.getBrokerId());
    assertEquals("CODE001", dto.getCode());
    assertEquals("Test Description", dto.getDescription());
    assertEquals("OrgType001", dto.getOrgType());
    assertEquals("MacroArea001", dto.getMacroArea());
    assertEquals("ServiceType001", dto.getServiceType());
    assertEquals("Collecting Reason 001", dto.getCollectingReason());
    assertEquals("TaxonomyCode001", dto.getTaxonomyCode());
    assertTrue(dto.getFlagAnonymousFiscalCode());
    assertFalse(dto.getFlagMandatoryDueDate());
    assertTrue(dto.getFlagNotifyIo());
    assertEquals("Test IO Template Message", dto.getIoTemplateMessage());
  }

  private static DebtPositionType getDebtPositionType() {
    DebtPositionType debtPositionType = new DebtPositionType();
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
    return debtPositionType;
  }

}
