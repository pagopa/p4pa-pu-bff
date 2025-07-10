package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.springframework.data.domain.Pageable;

public interface AssessmentsService {
  PagedAssessmentsView findPagedAssessmentsView(AssessmentsFiltersDTO filters, Pageable pageable, String accessToken);
  PagedModelAssessmentsDetail findPagedModelAssessmentsDetail(AssessmentsRowsDetailFiltersDTO filters, Pageable pageable, String accessToken);
  AssessmentsDetail findAssessmentsDetail(Long assessmentDetailId,String accessToken);
  Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode, String accessToken);
}
