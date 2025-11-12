package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.AnalyticsApi;
import it.gov.pagopa.pu.bff.dto.generated.SupersetUrlResponseDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.analytics.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AnalyticsController implements AnalyticsApi {

  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @Override
  public ResponseEntity<SupersetUrlResponseDTO> generateSupersetUrl(Long organizationId) {
    log.info("Request superset url for organizationId {}", organizationId);
    String supersetUrl = analyticsService.generateSupersetUrl(organizationId,
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken());

    return ResponseEntity.ok(SupersetUrlResponseDTO.builder()
      .authorizedUrl(supersetUrl).build());
  }
}
