package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsDetailService;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AssessmentsDetailRetrieverServiceImpl implements AssessmentsDetailRetrieverService {
  private final AssessmentsService assessmentsService;
  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
  private final AssessmentsDetailService assessmentsDetailService;


  public AssessmentsDetailRetrieverServiceImpl(AssessmentsService assessmentsService, DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService, AssessmentsDetailService assessmentsDetailService) {
    this.assessmentsService = assessmentsService;
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
    this.assessmentsDetailService = assessmentsDetailService;
  }

  @Override
  public List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    if (CollectionUtils.isEmpty(createAssessmentsDetail.getIuds())) {
      throw new IllegalArgumentException("iuds must not be empty or null");
    }
    Assessments assessments = assessmentsService.getAssessmentsById(assessmentId, accessToken);
    validateAssessments(organizationId, assessmentId, assessments, loggedUser, accessToken);

    debtPositionTypeOrgRetrieverService.validateIuds(organizationId, assessments.getDebtPositionTypeOrgCode(), createAssessmentsDetail.getIuds(), accessToken);
    return assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId, createAssessmentsDetail, accessToken);
  }

  private void validateAssessments(Long organizationId, Long assessmentId, Assessments assessments, UserInfo loggedUser, String accessToken) {
    if (assessments == null || !organizationId.equals(assessments.getOrganizationId())) {
      throw new ResourceNotFoundException("Assessments having assessmentId " + assessmentId + " and organizationId " + organizationId + " not found");
    }
    debtPositionTypeOrgRetrieverService.validateOperator(organizationId, assessments.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
  }

  @Override
  public void deleteAssessmentsDetails(Long organizationId, List<Long> assessmentDetailIds, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    validateAllIdsExistAndBelongToOrganization(organizationId, assessmentDetailIds, accessToken);

    for (Long id : assessmentDetailIds) {
      deleteSingleAssessmentDetail(organizationId, id, loggedUser, accessToken);
    }
  }

  private void validateAllIdsExistAndBelongToOrganization(Long organizationId, List<Long> assessmentDetailIds, String accessToken) {
    List<Long> notFoundIds = assessmentDetailIds.stream()
      .filter(Objects::nonNull)
      .filter(id -> {
        AssessmentsDetail detail = assessmentsService.findAssessmentsDetail(id, accessToken);
        return detail == null || !organizationId.equals(detail.getOrganizationId());
      })
      .toList();

    if (!notFoundIds.isEmpty()) {
      throw new ResourceNotFoundException("The following assessmentDetailIds were not found or do not belong to organization: " + notFoundIds);
    }
  }

  private void deleteSingleAssessmentDetail(Long organizationId, Long id, UserInfo loggedUser, String accessToken) {
    AssessmentsDetail detail = assessmentsService.findAssessmentsDetail(id, accessToken);
    debtPositionTypeOrgRetrieverService.validateOperator(organizationId, detail.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
    assessmentsDetailService.deleteAssessmentsDetails(id, accessToken);
  }
}

