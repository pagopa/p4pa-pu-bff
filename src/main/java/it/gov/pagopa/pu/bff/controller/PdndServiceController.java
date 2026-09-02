package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PdndServiceApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.pdnd_service.PdndServiceRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.PdndService;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceType;
import lombok.extern.slf4j.Slf4j;
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
  public ResponseEntity<PdndServiceDTO> getPdndService(Long organizationId, String purposeId, String subUnitCode) {
    log.info("User requested getPdndService having organizationId {} and purposeId {}", organizationId, purposeId);
    return ResponseEntity.ok(pdndServiceRetrieverService.getPdndService(organizationId, purposeId, subUnitCode, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<PdndServiceDTO>> getPdndServices(Long organizationId, String subUnitCode, PdndServiceType serviceType) {
    log.info("User requested getPdndServices having organizationId {} and serviceType {}", organizationId, serviceType);
    return ResponseEntity.ok(pdndServiceRetrieverService.getPdndServices(organizationId, subUnitCode, serviceType, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<PdndService>> getPdndClientServices(Long organizationId, String clientId, PdndServiceType serviceType) {
    log.info("User requested pdndClient's services having organizationId {} and clientId {}", organizationId, clientId);
    return ResponseEntity.ok(pdndServiceRetrieverService.getPdndClientServices(organizationId, clientId, serviceType, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
