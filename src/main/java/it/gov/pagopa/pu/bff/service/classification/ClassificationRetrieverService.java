package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;

public interface ClassificationRetrieverService {
  ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, UserInfo loggedUser, String accessToken);
}
