package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DebtPositionTypeOrgDTOMapperTest {

  private final DebtPositionTypeOrgDTOMapper mapper = Mappers.getMapper(DebtPositionTypeOrgDTOMapper.class);

  @Test
  void givenDtoWhenMapThenMapIt() {
    DebtPositionTypeOrg dto = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);

    DebtPositionTypeOrgDTO result = mapper.map(dto, "DebtPositionTypeDescription", "DebtPositionTypeCode", "NotifyOutcomePushOrgSilServiceApplicationName", "AmountActualizationOrgSilServiceApplicationName", "spontaneousFormCode");

    TestUtils.reflectionEqualsByName(dto, result);
    TestUtils.checkNotNullFields(result);

    assertEquals("DebtPositionTypeDescription", result.getDebtPositionTypeDescription());
    assertEquals("DebtPositionTypeCode", result.getDebtPositionTypeCode());
    assertEquals("NotifyOutcomePushOrgSilServiceApplicationName", result.getNotifyOutcomePushOrgSilServiceApplicationName());
    assertEquals("AmountActualizationOrgSilServiceApplicationName", result.getAmountActualizationOrgSilServiceApplicationName());
  }

  @Test
  void givenDebtPositionTypeWhenMapThenMapItCorrectly() {
    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    DebtPositionType debtPositionType = new DebtPositionType();
    debtPositionType.setDescription("Test Description");
    debtPositionType.setCode("Test Code");

    DebtPositionTypeOrgDTO result = mapper.map(debtPositionTypeOrg, debtPositionType, "NotifyOutcomePushOrgSilServiceApplicationName", "AmountActualizationOrgSilServiceApplicationName", "spontaneousFormCode");

    TestUtils.reflectionEqualsByName(debtPositionTypeOrg, result);
    TestUtils.checkNotNullFields(result);

    assertEquals("Test Description", result.getDebtPositionTypeDescription());
    assertEquals("Test Code", result.getDebtPositionTypeCode());
    assertEquals("NotifyOutcomePushOrgSilServiceApplicationName", result.getNotifyOutcomePushOrgSilServiceApplicationName());
    assertEquals("AmountActualizationOrgSilServiceApplicationName", result.getAmountActualizationOrgSilServiceApplicationName());
  }

  @Test
  void givenNullDebtPositionTypeAndNullApplicationNamesWhenMapThenMapWithNullFields() {
    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);

    DebtPositionTypeOrgDTO result = mapper.map(debtPositionTypeOrg, null, null, null, null);

    TestUtils.reflectionEqualsByName(debtPositionTypeOrg, result);

    assertNull(result.getDebtPositionTypeDescription());
    assertNull(result.getDebtPositionTypeCode());
    assertNull(result.getNotifyOutcomePushOrgSilServiceApplicationName());
    assertNull(result.getAmountActualizationOrgSilServiceApplicationName());
  }
}
