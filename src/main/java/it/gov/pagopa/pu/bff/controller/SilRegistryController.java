package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.SilRegistryApi;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedSilRegistry;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.sil_registry.SilRegistryRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.RegistryOutcome;
import it.gov.pagopa.pu.registries.dto.generated.RegistrySilEventType;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

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

  @Override
  public ResponseEntity<PagedSilRegistry> getSilRegistries(Long organizationId, RegistrySilEventType eventType, OffsetDateTime eventDateFrom, OffsetDateTime eventDateTo, String iuv, RegistryOutcome outcome, Pageable pageable) {
    log.info("User requested getSilRegistries having organizationId {}", organizationId);
    return ResponseEntity.ok(silRegistryRetrieverService.getSilRegistries(
      organizationId,
      SilRegistryFiltersDTO.builder()
        .eventType(eventType)
        .eventDate(new OffsetDateTimeIntervalFilter(eventDateFrom, eventDateTo))
        .iuv(iuv)
        .outcome(outcome)
        .build(),
      pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
