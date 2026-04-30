package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsDetailClient;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentsDetailServiceImpl implements AssessmentsDetailService {

  private final AssessmentsDetailClient assessmentsDetailClient;

  public AssessmentsDetailServiceImpl(AssessmentsDetailClient assessmentsDetailClient) {
    this.assessmentsDetailClient = assessmentsDetailClient;
  }

  @Override
  public List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentsId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    return assessmentsDetailClient.createAssessmentsDetail(organizationId, assessmentsId, createAssessmentsDetail, accessToken);
  }

  @Override
  public void deleteAssessmentsDetails(Long assessmentDetailId, String accessToken) {
    assessmentsDetailClient.deleteAssessmentsDetails(assessmentDetailId, accessToken);
  }
}
