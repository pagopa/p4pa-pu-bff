package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.MonitoringApi;
import it.gov.pagopa.pu.bff.dto.generated.ServiceStatus;
import it.gov.pagopa.pu.bff.service.CoreHealthIndicatorService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CoreHealthIndicatorController implements MonitoringApi {

  private final CoreHealthIndicatorService coreHealthIndicatorService;

  public CoreHealthIndicatorController(
    CoreHealthIndicatorService coreHealthIndicatorService) {
    this.coreHealthIndicatorService = coreHealthIndicatorService;
  }

  @Override
  public ResponseEntity<List<ServiceStatus>> getHealthStatus() {
    log.info("Check services status");
    return ResponseEntity.ok(coreHealthIndicatorService.getStatus());
  }
}
