package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.model.ServiceStatus;
import it.gov.pagopa.pu.bff.service.HealthIndicatorService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthIndicatorController {

  @Autowired
  HealthIndicatorService healthIndicatorService;

  @GetMapping("/health")
  public CompletableFuture<List<ServiceStatus>> getStatus() {
    log.info("Check services status");
    return healthIndicatorService.getStatus();
  }
}
