package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.config.MonitoringServiceConf;
import it.gov.pagopa.pu.bff.model.ServiceStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HealthIndicatorService {

  private final MonitoringServiceConf monitoringServiceConf;
  private final RestTemplate restTemplate = new RestTemplate();

  public HealthIndicatorService(MonitoringServiceConf monitoringServiceConf) {
    this.monitoringServiceConf = monitoringServiceConf;
  }

  @Async
  public CompletableFuture<List<ServiceStatus>> getStatus() {
    CompletableFuture<List<ServiceStatus>> future = new CompletableFuture<>();
    List<ServiceStatus> currentStatus = getServiceStatuses();
    if (!currentStatus.isEmpty()) {
      future.complete(currentStatus);
    } else {
      CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
        if (!future.isDone()) {
          future.complete(List.of(new ServiceStatus("Max-Retry",null, "No updates within 30 seconds")));
        }
      });
    }

    return future;
  }

  private List<ServiceStatus> getServiceStatuses() {
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
        return new ServiceStatus(serviceName, url, response);
      } catch (Exception e) {
        return new ServiceStatus(serviceName, url,"Unreachable: " + e.getMessage());
      }
    });
  }
}
