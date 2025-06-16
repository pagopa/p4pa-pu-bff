package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class PagedAssessmentExtendedDTOMapper {

  private final AssessmentExtendedDTOMapper assessmentExtendedDTOMapper;

  public PagedAssessmentExtendedDTOMapper(AssessmentExtendedDTOMapper assessmentExtendedDTOMapper) {
    this.assessmentExtendedDTOMapper = assessmentExtendedDTOMapper;
  }

  public PagedAssessmentsExtendedDTO map(PagedAssessmentsView assessmentsPage, Map<String, String> debtPositionTypeOrgMap){
    PagedAssessmentsExtendedDTO mappedAssessmentsExtendedDTO = new PagedAssessmentsExtendedDTO();

    if(assessmentsPage != null){
      if (!assessmentsPage.getContent().isEmpty()){

        mappedAssessmentsExtendedDTO.setContent(
          assessmentsPage.getContent().stream()
          .map(assessment -> {
            String code = assessment.getDebtPositionTypeOrgCode();
            String description = debtPositionTypeOrgMap.getOrDefault(code, "");
            return assessmentExtendedDTOMapper.mapToAssessmentsExtendedDTO(assessment, description);
            })
            .toList());

      }else {
        mappedAssessmentsExtendedDTO.setContent(Collections.emptyList());
      }

       mappedAssessmentsExtendedDTO.setTotalPages(assessmentsPage.getTotalPages());
       mappedAssessmentsExtendedDTO.setSize(assessmentsPage.getSize());
       mappedAssessmentsExtendedDTO.setNumber(assessmentsPage.getNumber());
       mappedAssessmentsExtendedDTO.setTotalElements(assessmentsPage.getTotalElements());

    }
    return mappedAssessmentsExtendedDTO;
  }

}
