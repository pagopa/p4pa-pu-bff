package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsEntityExtendedClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssessmentsServiceImpl implements AssessmentsService{

  private final AssessmentsClient assessmentsClient;
  private final AssessmentsEntityExtendedClient assessmentsEntityExtendedClient;

  public AssessmentsServiceImpl(AssessmentsClient assessmentsClient, AssessmentsEntityExtendedClient assessmentsEntityExtendedClient) {
    this.assessmentsClient = assessmentsClient;
      this.assessmentsEntityExtendedClient = assessmentsEntityExtendedClient;
  }

  @Override
  public PagedAssessmentsView findPagedAssessmentsView(AssessmentsFiltersDTO filters, Pageable pageable, String accessToken) {
    return assessmentsClient.findPagedAssessmentsView(filters, pageable, accessToken);
  }

  @Override
  public PagedModelAssessmentsDetail findPagedModelAssessmentsDetail(AssessmentsRowsDetailFiltersDTO filters, Pageable pageable, String accessToken) {
    return assessmentsClient.findPagedModelAssessmentsDetail(filters, pageable, accessToken);
  }

  @Override
  public AssessmentsDetail findAssessmentsDetail(Long assessmentDetailId, String accessToken) {
    return assessmentsClient.findAssessmentsDetail(assessmentDetailId, accessToken);
  }

  @Override
  public Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode, String accessToken) {
    return assessmentsClient.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, accessToken);
  }

  @Override
  public Assessments getAssessmentsById(Long assessmentId, String accessToken) {
    return assessmentsClient.getAssessmentsById(assessmentId, accessToken);
  }

  @Override
  public void updateStatus(Long organizationId, Long assessmentId, AssessmentStatus status, String accessToken) {
    assessmentsEntityExtendedClient.updateStatus(organizationId, assessmentId, status, accessToken);
  }
}
