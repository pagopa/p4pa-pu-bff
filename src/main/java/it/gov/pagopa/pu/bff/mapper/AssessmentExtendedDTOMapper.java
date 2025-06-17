package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface AssessmentExtendedDTOMapper {

  @Mapping(target = "content", expression = "java(mapContentWithDescriptions(source.getContent(), debtPositionTypeOrgMap))")
  @Mapping(target = "totalPages", source = "source.totalPages")
  @Mapping(target = "size", source = "source.size")
  @Mapping(target = "number", source = "source.number")
  @Mapping(target = "totalElements", source = "source.totalElements")
  PagedAssessmentsExtendedDTO mapToPagedAssessmentsExtendedDTO(PagedAssessmentsView source, Map<String, String> debtPositionTypeOrgMap);

  default List<AssessmentsExtendedDTO> mapContentWithDescriptions(List<Assessments> content, Map<String, String> debtPositionTypeOrgMap) {
   if (content == null) return Collections.emptyList();

   return content.stream().map(assessment -> {
       AssessmentsExtendedDTO dto = map(assessment);
       String code = assessment.getDebtPositionTypeOrgCode();
       String description = debtPositionTypeOrgMap.get(code);
       dto.setDescriptionDebtPositionTypeOrgCode(description);
       return dto;
     }).toList();
   }

  AssessmentsExtendedDTO map(Assessments assessments);
}


