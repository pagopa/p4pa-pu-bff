package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PdndClientApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.pdnd_client.PdndClientRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndClientNoSecretDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PdndClientController implements PdndClientApi {

  private final PdndClientRetrieverService pdndClientRetrieverService;

  public PdndClientController(PdndClientRetrieverService pdndClientRetrieverService) {
    this.pdndClientRetrieverService = pdndClientRetrieverService;
  }

  @Override
  public ResponseEntity<List<PdndClientNoSecretDTO>> getPdndClientsByOrgSubUnitCode(Long organizationId, String orgSubUnitCode) {
    log.info("User requested getPdndClientsByOrgSubUnitCode having organizationId {} and orgSubUnitCode {}", organizationId, orgSubUnitCode);
    return ResponseEntity.ok(pdndClientRetrieverService.getPdndClientsByOrganizationIdAndSubUnitCode(organizationId, orgSubUnitCode, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PdndClientNoSecretDTO> getPdndClient(Long organizationId, String clientId) {
    log.info("User requested getPdndClient having organizationId {} and clientId {}", organizationId, clientId);
    return ResponseEntity.ok(pdndClientRetrieverService.getPdndClient(organizationId, clientId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PdndClientNoSecretDTO> createPdndClient(Long organizationId, PdndClientDTO body) {
    log.info("User requested createPdndClient having organizationId {}", organizationId);
    return ResponseEntity.ok(pdndClientRetrieverService.createPdndClient(organizationId, body, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deletePdndClient(Long organizationId, String clientId) {
    log.info("User requested to delete PdndClient having organizationId {} and clientId {}", organizationId, clientId);
    pdndClientRetrieverService.deletePdndClient(organizationId, clientId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }
}
