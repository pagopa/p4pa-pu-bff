package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.config.MonitoringServiceConf;
import it.gov.pagopa.pu.bff.dto.generated.ServiceStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
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

  @Async
  public List<ServiceStatus> getStatus() {
    List<CompletableFuture<ServiceStatus>> checks = monitoringServiceConf.getServices().values().stream()
      .map(serviceConfig -> checkServiceAsync(serviceConfig.getUrl(),
        serviceConfig.getServiceName()))
      .toList();

    return CompletableFuture.allOf(checks.toArray(new CompletableFuture[0]))
      .thenApply(v -> checks.stream()
        .map(CompletableFuture::join)
        .toList()
      ).join();
  }

  private CompletableFuture<ServiceStatus> checkServiceAsync(String url, String serviceName) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        String response = restTemplate.getForObject(url, String.class);
        return new ServiceStatus(serviceName, response);
      } catch (Exception e) {
        return new ServiceStatus(serviceName,"Unreachable: " + e.getMessage());
      }
    });
  }
}
