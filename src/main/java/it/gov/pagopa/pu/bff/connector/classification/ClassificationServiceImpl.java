package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.ClassificationSearchClient;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import org.springframework.stereotype.Service;

@Service
public class ClassificationServiceImpl implements ClassificationService {
  private final ClassificationSearchClient classificationSearchClient;

  public ClassificationServiceImpl(ClassificationSearchClient classificationSearchClient) {
    this.classificationSearchClient = classificationSearchClient;
  }

  @Override
  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken) {
    return classificationSearchClient.getClassificationDetail(organizationId, classificationId, accessToken);
  }
}
