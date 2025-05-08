package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.ClassificationClient;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import org.springframework.stereotype.Service;

@Service
public class ClassificationServiceImpl implements ClassificationService {
  private final ClassificationClient classificationClient;

  public ClassificationServiceImpl(ClassificationClient classificationClient) {
    this.classificationClient = classificationClient;
  }

  @Override
  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken) {
    return classificationClient.getClassificationDetail(organizationId, classificationId, accessToken);
  }
}
