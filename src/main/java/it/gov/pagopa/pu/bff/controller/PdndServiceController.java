package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PdndServiceApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.pdnd_service.PdndServiceRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PdndServiceController implements PdndServiceApi {

  private final PdndServiceRetrieverService pdndServiceRetrieverService;

  public PdndServiceController(PdndServiceRetrieverService pdndServiceRetrieverService) {
    this.pdndServiceRetrieverService = pdndServiceRetrieverService;
  }

  @Override
  public ResponseEntity<PdndService> createPdndService(Long organizationId, PdndServiceRequestDTO body, String subUnitCode) {
    log.info("User requested savePdndService having organizationId {}", organizationId);
    return ResponseEntity.ok(pdndServiceRetrieverService.createPdndService(organizationId, body, subUnitCode, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
