package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;

public interface ClassificationService {
  ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, String accessToken);
}
