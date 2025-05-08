package it.gov.pagopa.pu.bff.service.classification;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import org.springframework.stereotype.Service;

@Service
public class ClassificationRetrieverServiceImpl implements ClassificationRetrieverService {
  private final ClassificationService classificationService;

  public ClassificationRetrieverServiceImpl(ClassificationService classificationService) {
    this.classificationService = classificationService;
  }

  @Override
  public ClassificationDetailViewDTO getClassificationDetail(Long organizationId, Long classificationId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);

    return classificationService.getClassificationDetail(organizationId, classificationId, accessToken);
  }
}
