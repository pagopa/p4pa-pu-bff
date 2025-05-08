package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class ClassificationClient {
  private final ClassificationApisHolder classificationApisHolder;

  public ClassificationClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken) {
    try {
      return classificationApisHolder.getClassificationsApi(accessToken)
        .getClassificationDetail(organizationId, classificationId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("ClassificationDetail with organizationId {} and classificationId {} not found", organizationId, classificationId);
      return null;
    }
  }
}
