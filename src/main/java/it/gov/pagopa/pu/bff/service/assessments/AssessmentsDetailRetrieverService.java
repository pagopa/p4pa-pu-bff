package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;

import java.util.List;

public interface AssessmentsDetailRetrieverService {
  List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail, UserInfo loggedUser, String accessToken);
}
