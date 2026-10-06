package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PdndServiceApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.pdnd_service.PdndServiceRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

  @Override
  public ResponseEntity<PdndServiceView> getPdndService(Long organizationId, String purposeId, String subUnitCode) {
    log.info("User requested getPdndService having organizationId {} and purposeId {}", organizationId, purposeId);
    return ResponseEntity.ok(pdndServiceRetrieverService.getPdndService(organizationId, purposeId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<PdndServiceView>> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType serviceType) {
    log.info("User requested getPdndServices having organizationId {} and serviceType {}", organizationId, serviceType);
    return ResponseEntity.ok(pdndServiceRetrieverService.getPdndServices(organizationId, subUnitCode, serviceType, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deletePdndService(Long organizationId, String purposeId, String subUnitCode) {
    log.info("User requested deletePdndService having organizationId {} and purposeId {}", organizationId, purposeId);
    pdndServiceRetrieverService.deletePdndService(organizationId, purposeId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<PagedPdndServiceView> getOrgSubUnitsPdndServices(Long organizationId, String subUnitCode, String subUnitName, Pageable pageable) {
    log.info("User requested getOrgSubUnitsPdndServices having organizationId {}", organizationId);
    return ResponseEntity.ok(pdndServiceRetrieverService.getOrgSubUnitsPdndServices(organizationId, subUnitCode, subUnitName, pageable,
      SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<PdndService>> getPdndClientServices(Long organizationId, String clientId, PdndServiceType serviceType) {
    log.info("User requested pdndClient's services having organizationId {} and clientId {}", organizationId, clientId);
    return ResponseEntity.ok(pdndServiceRetrieverService.getPdndClientServices(organizationId, clientId, serviceType, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
