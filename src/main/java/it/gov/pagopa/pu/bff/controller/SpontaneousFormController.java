package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.SpontaneousFormsApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SpontaneousFormController implements SpontaneousFormsApi {

  private final SpontaneousFormRetrieverService spontaneousFormRetrieverService;

  public SpontaneousFormController(SpontaneousFormRetrieverService spontaneousFormRetrieverService) {
    this.spontaneousFormRetrieverService = spontaneousFormRetrieverService;
  }

  @Override
  public ResponseEntity<List<SpontaneousForm>> getSpontaneousForms(Long organizationId) {
    log.info("User requested getSpontaneousForms having organizationId {}", organizationId);
    return ResponseEntity.ok(spontaneousFormRetrieverService.getSpontaneousForms(organizationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}