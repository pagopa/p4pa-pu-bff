package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentExtendedDTOMapper {

  @Mapping(target = "descriptionDebtPositionTypeOrgCode", source = "descriptionDebtPositionTypeOrgCode")
  AssessmentsExtendedDTO mapToAssessmentsExtendedDTO(Assessments assessments, String descriptionDebtPositionTypeOrgCode);
}
