package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;

import java.util.List;

public interface AssessmentsDetailService {
  List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentsId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken);

  void deleteAssessmentsDetails(Long assessmentDetailId, String accessToken);
}
