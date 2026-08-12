package it.gov.pagopa.pu.bff.connector.workflow_hub.config;

import it.gov.pagopa.pu.bff.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.bff.connector.workflow_hub.mapper.WorkflowErrorDTOMapper;
import it.gov.pagopa.pu.workflowhub.generated.ApiClient;
import it.gov.pagopa.pu.workflowhub.generated.BaseApi;
import it.gov.pagopa.pu.workflowhub.client.generated.ScheduleApi;
import it.gov.pagopa.pu.workflowhub.client.generated.TaxonomyApi;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowErrorDTO;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Service
public class WorkflowHubApisHolder {
  private final TaxonomyApi taxonomyApi;
  private final ScheduleApi scheduleApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public WorkflowHubApisHolder(
    WorkflowHubApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder,
    JsonMapper jsonMapper
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "WORKFLOW-HUB", clientConfig.isPrintBodyWhenError(),
      WorkflowErrorDTO.class, WorkflowErrorDTOMapper::map)
    );

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
