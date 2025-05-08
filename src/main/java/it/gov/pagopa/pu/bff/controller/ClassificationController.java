package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ClassificationController implements ClassificationsApi {
  private final ClassificationRetrieverService classificationRetrieverService;

  public ClassificationController(ClassificationRetrieverService classificationRetrieverService) {
    this.classificationRetrieverService = classificationRetrieverService;
  }

  @Override
  public ResponseEntity<ClassificationDetailViewDTO> getClassificationDetail(Long organizationId, Long classificationId) {
    log.info("User requested getClassificationDetail having organizationId {} and classificationId {}", organizationId, classificationId);

    return ResponseEntity.ofNullable(classificationRetrieverService.getClassificationDetail(
      organizationId, classificationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
