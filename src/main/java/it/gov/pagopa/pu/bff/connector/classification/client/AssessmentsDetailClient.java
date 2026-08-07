package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
public class AssessmentsDetailClient {

  private final ClassificationApisHolder classificationApisHolder;

  public AssessmentsDetailClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentsId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken){
      return classificationApisHolder.getAssessmentsDetailApi(accessToken)
        .createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail);
  }

  public void deleteAssessmentsDetails(Long assessmentDetailId, String accessToken) {
    try {
      classificationApisHolder.getAssessmentsDetailEntityControllerApi(accessToken)
        .crudDeleteAssessmentsdetail(String.valueOf(assessmentDetailId));
    } catch (HttpClientErrorException.NotFound e) {
      throw new NotFoundException("ASSESSMENT_DETAIL_NOT_FOUND", "AssessmentsDetail with ID %d not found".formatted(assessmentDetailId));
    }
  }
}
