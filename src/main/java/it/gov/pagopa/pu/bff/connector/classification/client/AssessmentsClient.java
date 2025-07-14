package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

@Service
@Slf4j
public class AssessmentsClient {

  private final ClassificationApisHolder classificationApisHolder;

  public AssessmentsClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public PagedAssessmentsView findPagedAssessmentsView(AssessmentsFiltersDTO filters, Pageable pageable, String accessToken){
    return classificationApisHolder.getAssessmentsControllerApi(accessToken)
      .getPagedAssessmentsList(
        filters.getAssessmentName(),
        filters.getUpdateDateFrom(),
        filters.getUpdateDateTo(),
        filters.getIuv(),
        new ArrayList<>(filters.getDebtPositionTypeOrgCodes()),
        filters.getStatus(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public PagedModelAssessmentsDetail findPagedModelAssessmentsDetail(AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO, Pageable pageable, String accessToken){
    return classificationApisHolder.getAssessmentsDetailSearchControllerApi(accessToken)
      .crudAssessmentsDetailsFindAssessmentsRowsDetail(
        assessmentsRowsDetailFiltersDTO.getAssessmentId(),
        assessmentsRowsDetailFiltersDTO.getIud(),
        assessmentsRowsDetailFiltersDTO.getIuv(),
        assessmentsRowsDetailFiltersDTO.getUpdateDateTimeIntervalFilter().getFrom(),
        assessmentsRowsDetailFiltersDTO.getUpdateDateTimeIntervalFilter().getTo(),
        assessmentsRowsDetailFiltersDTO.getPaymentDateTimeIntervalFilter().getFrom(),
        assessmentsRowsDetailFiltersDTO.getPaymentDateTimeIntervalFilter().getTo(),
        assessmentsRowsDetailFiltersDTO.getFiscalCode(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }

  public AssessmentsDetail findAssessmentsDetail(Long assessmentDetailId, String accessToken){
    try{
      return classificationApisHolder.getAssessmentsDetailEntityControllerApi(accessToken).crudGetAssessmentsdetail(assessmentDetailId.toString());
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Assessment detail with id %s not found".formatted(assessmentDetailId));
      return null;
    }
  }

  public Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode, String accessToken){
    return classificationApisHolder.getAssessmentsControllerApi(accessToken)
      .createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode);
  }

  public Assessments getAssessmentsById(Long assessmentId, String accessToken){
    try{
      return classificationApisHolder.getAssessmentsEntityControllerApi(accessToken)
              .crudGetAssessments(assessmentId.toString());
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Assessment with id %s not found".formatted(assessmentId));
      return null;
    }
  }
}
