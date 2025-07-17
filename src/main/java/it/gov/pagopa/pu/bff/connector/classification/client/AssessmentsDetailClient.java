package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
