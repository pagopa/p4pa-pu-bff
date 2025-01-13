package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.config.CoreServiceHealthStatusConfig;
import it.gov.pagopa.pu.bff.config.MonitoringServiceConf;
import it.gov.pagopa.pu.bff.dto.generated.ServiceStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoreHealthIndicatorService {

  private final MonitoringServiceConf monitoringServiceConf;
  private final RestTemplate restTemplate;

  public CoreHealthIndicatorService(MonitoringServiceConf monitoringServiceConf,
    RestTemplateBuilder restTemplateBuilder) {
    this.monitoringServiceConf = monitoringServiceConf;
    this.restTemplate = restTemplateBuilder.build();
  }

  public List<ServiceStatus> getStatus() {
    List<CompletableFuture<ServiceStatus>> checks = monitoringServiceConf.getServices().values().stream()
      .map(this::checkServiceAsync)
      .toList();

    return CompletableFuture.allOf(checks.toArray(new CompletableFuture[0]))
      .thenApply(v -> checks.stream()
        .map(CompletableFuture::join)
        .toList()
      ).join();
  }

  private CompletableFuture<ServiceStatus> checkServiceAsync(
    CoreServiceHealthStatusConfig coreServiceHealthStatusConfig) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        String response = restTemplate.getForObject(coreServiceHealthStatusConfig.getUrl(), String.class);
        return new ServiceStatus(coreServiceHealthStatusConfig.getServiceName(), response);
      } catch (Exception e) {
        return new ServiceStatus(coreServiceHealthStatusConfig.getServiceName(), "Unreachable: " + e.getMessage());
      }
    });
  }
}
