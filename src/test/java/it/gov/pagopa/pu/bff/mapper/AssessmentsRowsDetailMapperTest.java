package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AssessmentsRowsDetailMapperTest {

  private final AssessmentsRowsDetailMapper mapper = Mappers.getMapper(AssessmentsRowsDetailMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedAssessmentsRowsDetailThenCorrectMapping() {
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(
      PagedModelAssessmentsDetail.class);
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    String debtPositionTypeOrgDescription = "debtPositionTypeOrgDescription";

    AssessmentsRowsDetail result = mapper.map(pagedModelAssessmentsDetail, assessments, debtPositionTypeOrgDescription);

    assertNotNull(result);
    assertNotNull(result.getPagedAssessmentsRowsDetail());
    assertEquals(assessments.getAssessmentName(),result.getAssessmentsName());
    assertEquals(assessments.getStatus(),result.getStatus());
    assertEquals(assessments.getUpdateOperatorExternalId(),result.getUpdateOperatorExternalId());
    assertEquals(debtPositionTypeOrgDescription,result.getDebtPositionTypeOrgDescription());
    assertEquals(assessments.getFlagManualGeneration(), result.getFlagManualGeneration());
    assertEquals(assessments.getAssessmentId(), result.getAssessmentId());
    assertEquals(assessments.getDebtPositionTypeOrgCode(), result.getDebtPositionTypeOrgCode());
    assertEquals("", result.getName());
    assertEquals("", result.getFamilyName());
    TestUtils.checkNotNullFields(result);
  }
}
