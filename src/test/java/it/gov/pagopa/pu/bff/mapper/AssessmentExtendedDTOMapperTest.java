package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class AssessmentExtendedDTOMapperTest {

  private final PodamFactory podamFactory = new PodamFactoryImpl();

  AssessmentExtendedDTOMapper mapper = Mappers.getMapper(AssessmentExtendedDTOMapper.class);

  @Test
  void givenWhenMapToAssessmentsExtendedDTOThen() {
    //given
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    AssessmentsExtendedDTO assessmentsExtendedDTO = AssessmentsExtendedDTO.builder()
      .assessmentId(assessments.getAssessmentId())
      .assessmentName(assessments.getAssessmentName())
      .creationDate(assessments.getCreationDate())
      .updateDate(assessments.getUpdateDate())
      .debtPositionTypeOrgCode(assessments.getDebtPositionTypeOrgCode())
      .flagManualGeneration(assessments.getFlagManualGeneration())
      .organizationId(assessments.getOrganizationId())
      .links(assessments.getLinks())
      .printed(assessments.getPrinted())
      .status(assessments.getStatus())
      .updateOperatorExternalId(assessments.getUpdateOperatorExternalId())
      .updateTraceId(assessments.getUpdateTraceId())
      .descriptionDebtPositionTypeOrgCode("description")
      .build();

    //when
    AssessmentsExtendedDTO result = mapper.mapToAssessmentsExtendedDTO(assessments, "description");
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(assessmentsExtendedDTO, result);
    TestUtils.checkNotNullFields(result);
    TestUtils.reflectionEqualsByName(assessmentsExtendedDTO, result);
  }
}
