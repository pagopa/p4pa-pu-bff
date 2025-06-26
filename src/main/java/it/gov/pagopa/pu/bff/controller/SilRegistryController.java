package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.SilRegistryApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SilRegistryController implements SilRegistryApi {
  private final SilRegistryRetrieverService silRegistryRetrieverService;

  public SilRegistryController(SilRegistryRetrieverService silRegistryRetrieverService) {
    this.silRegistryRetrieverService = silRegistryRetrieverService;
  }

  @Override
  public ResponseEntity<SilRegistryDTO> getSilRegistry(Long organizationId, String registryId) {
    log.info("User requested getSilRegistry having organizationId {} and registryId {}", organizationId, registryId);
    return ResponseEntity.ofNullable(silRegistryRetrieverService.getSilRegistry(
      organizationId, registryId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
