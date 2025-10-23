package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.SpontaneousFormsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

  @Override
  public ResponseEntity<PagedSpontaneousForm> getPagedSpontaneousForms(Long organizationId, String code, Pageable pageable) {
    log.info("User requested getPagedSpontaneousForms having organizationId {}", organizationId);
    return ResponseEntity.ok(spontaneousFormRetrieverService.getPagedSpontaneousForms(organizationId, code, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<SpontaneousForm> getSpontaneousFormDetail(Long organizationId, Long spontaneousFormId) {
    log.info("User requested getSpontaneousFormDetail having organizationId {} and spontaneousFormId {}", organizationId, spontaneousFormId);
    return ResponseEntity.ok(spontaneousFormRetrieverService.getSpontaneousFormDetail(organizationId, spontaneousFormId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<SpontaneousForm> createSpontaneousForm(Long organizationId, SpontaneousForm spontaneousForm) {
    log.info("User requested createSpontaneousForm having organizationId {}", organizationId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(spontaneousFormRetrieverService.createSpontaneousForm(organizationId, spontaneousForm, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deleteSpontaneousForm(Long organizationId, Long spontaneousFormId) {
    log.info("User requested deleteSpontaneousForm having organizationId {} and spontaneousFormId {}", organizationId, spontaneousFormId);
    spontaneousFormRetrieverService.deleteSpontaneousForm(organizationId, spontaneousFormId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }
}