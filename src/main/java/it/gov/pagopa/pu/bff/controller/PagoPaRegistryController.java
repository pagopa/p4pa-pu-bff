package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.PagoPaRegistryApi;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.pagopa_registry.PagoPaRegistryRetrieverService;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import it.gov.pagopa.pu.registries.dto.generated.RegistryOutcome;
import it.gov.pagopa.pu.registries.dto.generated.RegistryPagoPaEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@Slf4j
@RestController
public class PagoPaRegistryController implements PagoPaRegistryApi {
  private final PagoPaRegistryRetrieverService pagoPaRegistryRetrieverService;

    public PagoPaRegistryController(PagoPaRegistryRetrieverService pagoPaRegistryRetrieverService) {
        this.pagoPaRegistryRetrieverService = pagoPaRegistryRetrieverService;
    }

  @Override
  public ResponseEntity<PagedPagoPaRegistry> getPagoPaRegistries(Long organizationId, RegistryPagoPaEventType eventType, OffsetDateTime eventDateFrom, OffsetDateTime eventDateTo, String iuv, RegistryOutcome outcome,  Pageable pageable) {
    log.info("User requested getPagoPaRegistries having organizationId {}", organizationId);
    return ResponseEntity.ok(pagoPaRegistryRetrieverService.getPagoPaRegistries(
            organizationId,
            PagoPaRegistryFiltersDTO.builder()
                    .eventType(eventType)
                    .eventDate(new OffsetDateTimeIntervalFilter(eventDateFrom,eventDateTo))
                    .iuv(iuv)
                    .outcome(outcome)
                    .build(),
            pageable, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagoPaRegistryDTO> getPagoPaRegistry(Long organizationId, String pagoPaRegistryId) {
    log.info("User requested getPagoPaRegistry having organizationId {} and pagoPaRegistryId {}", organizationId, pagoPaRegistryId);
    return ResponseEntity.ofNullable(pagoPaRegistryRetrieverService.getPagoPaRegistry(
            organizationId,pagoPaRegistryId, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }
}
