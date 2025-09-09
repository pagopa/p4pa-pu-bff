package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssessmentsRowsDetailMapper {

  @Mapping(target = "pagedAssessmentsRowsDetail", source = "pagedModelAssessmentsDetail")
  @Mapping(target = "assessmentId", source = "assessments.assessmentId")
  @Mapping(target = "assessmentsName", source = "assessments.assessmentName")
  @Mapping(target = "status", source = "assessments.status")
  @Mapping(target = "debtPositionTypeOrgCode", source = "assessments.debtPositionTypeOrgCode")
  @Mapping(target = "debtPositionTypeOrgDescription", source = "debtPositionTypeOrgDescription")
  @Mapping(target = "updateOperatorExternalId", source = "assessments.updateOperatorExternalId")
  @Mapping(target = "flagManualGeneration", source = "assessments.flagManualGeneration")
  AssessmentsRowsDetail map(PagedModelAssessmentsDetail pagedModelAssessmentsDetail, Assessments assessments, String debtPositionTypeOrgDescription);

  default PagedAssessmentsRowsDetail mapToPagedAssessmentsRowsDetail(PagedModelAssessmentsDetail pagedModelAssessmentsDetail) {
    return Mappers.getMapper(PagedAssessmentsRowsDetailMapper.class).map(pagedModelAssessmentsDetail);
  }
}
