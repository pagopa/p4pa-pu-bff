package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import org.springframework.data.domain.Pageable;

public interface AssessmentsRetrieverService {
  PagedAssessmentsExtendedDTO getPagedAssessmentsExtendedDTO(AssessmentsFiltersDTO assessmentsFiltersDTO, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken);
  PagedAssessmentsRowsDetail getPagedAssessmentsRowsDetail(AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);
  AssessmentsDetail getAssessmentsDetail(Long organizationId, Long assessmentId, Long assessmentDetailId, UserInfo loggedUser, String accessToken);
  Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode, UserInfo loggedUser, String accessToken);
}
