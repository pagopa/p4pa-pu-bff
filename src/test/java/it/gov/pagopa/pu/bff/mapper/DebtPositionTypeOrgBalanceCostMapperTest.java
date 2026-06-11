package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostMapperTest {
  private DebtPositionTypeOrgBalanceCostMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new DebtPositionTypeOrgBalanceCostMapper();
  }

  @Test
  void givenValidDebtPositionTypeOrgBalanceCostWhenMapThenCorrectMapping() {
    DebtPositionTypeOrgBalanceCost source = new DebtPositionTypeOrgBalanceCost();

    source.setType(DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST);
    source.setOperatingYear("2026");
    source.setOfficeCode("officeCode");
    source.setOfficeDescription("officeDescription");
    source.setSectionCode("sectionCode");
    source.setSectionDescription("sectionDescription");
    source.setAssessmentCode("assessmentCode");
    source.setAssessmentDescription("assessmentDescription");

    DebtPositionTypeOrgBalanceCostDTO result = mapper.map(source);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
    assertEquals(source.getType(), result.getType());
    assertEquals(source.getOperatingYear(), result.getOperatingYear());
    assertEquals(source.getOfficeCode(), result.getOfficeCode());
    assertEquals(source.getOfficeDescription(), result.getOfficeDescription());
    assertEquals(source.getSectionCode(), result.getSectionCode());
    assertEquals(source.getSectionDescription(), result.getSectionDescription());
    assertEquals(source.getAssessmentCode(), result.getAssessmentCode());
    assertEquals(source.getAssessmentDescription(), result.getAssessmentDescription());
  }
}
