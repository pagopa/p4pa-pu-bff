package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssessmentsRegistryClient {
  private final ClassificationApisHolder classificationApisHolder;

  public AssessmentsRegistryClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public AssessmentsRegistry createAssessmentsRegistry(AssessmentsRegistry assessmentsRegistry, String accessToken) {
    return classificationApisHolder.getAssessmentsRegistryApi(accessToken)
      .createAssessmentsRegistry(assessmentsRegistry);
  }

  public AssessmentsRegistry updateAssessmentsRegistry(AssessmentsRegistry body, String accessToken) {
    return classificationApisHolder.getAssessmentsRegistryEntityControllerApi(accessToken)
      .crudUpdateAssessmentsregistry(String.valueOf(body.getAssessmentRegistryId()), body);
  }
}
