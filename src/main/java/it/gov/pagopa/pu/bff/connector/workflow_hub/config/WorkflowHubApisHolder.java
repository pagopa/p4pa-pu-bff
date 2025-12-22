package it.gov.pagopa.pu.bff.connector.workflow_hub.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.workflowhub.controller.ApiClient;
import it.gov.pagopa.pu.workflowhub.controller.BaseApi;
import it.gov.pagopa.pu.workflowhub.controller.generated.ScheduleApi;
import it.gov.pagopa.pu.workflowhub.controller.generated.TaxonomyApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowHubApisHolder {
  private final TaxonomyApi taxonomyApi;
  private final ScheduleApi scheduleApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public WorkflowHubApisHolder(
    WorkflowHubApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    if (clientConfig.isPrintBodyWhenError()) {
      restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("WORKFLOW-HUB"));
    }

    this.taxonomyApi = new TaxonomyApi(apiClient);
    this.scheduleApi = new ScheduleApi(apiClient);
  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  public TaxonomyApi getTaxonomyApi(String accessToken) {
    return getApi(accessToken, taxonomyApi);
  }

  public ScheduleApi getScheduleApi(String accessToken) {
    return getApi(accessToken, scheduleApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
