package it.gov.pagopa.pu.bff.connector.process_executions.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.processexecutions.controller.ApiClient;
import it.gov.pagopa.pu.processexecutions.controller.BaseApi;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileControllerApi;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileSearchControllerApi;
import it.gov.pagopa.pu.processexecutions.controller.generated.IngestionFlowFileSearchControllerApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessExecutionsApisHolder {

    private final IngestionFlowFileSearchControllerApi ingestionFlowFileSearchControllerApi;
    private final ExportFileSearchControllerApi exportFileSearchControllerApi;
    private final ExportFileControllerApi exportFileControllerApi;
    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public ProcessExecutionsApisHolder(
        ProcessExecutionsApiClientConfig clientConfig,
        RestTemplateBuilder restTemplateBuilder
    ) {
      RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(clientConfig.getBaseUrl());
        apiClient.setBearerToken(bearerTokenHolder::get);
        apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
        apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
        if (clientConfig.isPrintBodyWhenError()) {
          restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("PROCESS-EXECUTIONS"));
        }

        this.ingestionFlowFileSearchControllerApi = new IngestionFlowFileSearchControllerApi(apiClient);
        this.exportFileSearchControllerApi = new ExportFileSearchControllerApi(apiClient);
        this.exportFileControllerApi = new ExportFileControllerApi(apiClient);
    }

    @PreDestroy
    public void unload(){
        bearerTokenHolder.remove();
    }

    /** It will return a {@link IngestionFlowFileSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public IngestionFlowFileSearchControllerApi getIngestionFlowFileSearchControllerApi(String accessToken){
        return getApi(accessToken, ingestionFlowFileSearchControllerApi);
    }

    /** It will return a {@link ExportFileSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public ExportFileSearchControllerApi getExportFileSearchControllerApi(String accessToken){
        return getApi(accessToken, exportFileSearchControllerApi);
    }

    /** It will return a {@link ExportFileControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public ExportFileControllerApi getExportFileControllerApi(String accessToken) {
      return getApi(accessToken, exportFileControllerApi);
    }

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
