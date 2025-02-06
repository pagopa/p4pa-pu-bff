package it.gov.pagopa.pu.bff.connector.process_executions.config;

import it.gov.pagopa.pu.processexecutions.controller.ApiClient;
import it.gov.pagopa.pu.processexecutions.controller.BaseApi;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileSearchControllerApi;
import it.gov.pagopa.pu.processexecutions.controller.generated.IngestionFlowFileSearchControllerApi;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class ProcessExecutionsApisHolder {

    private final IngestionFlowFileSearchControllerApi ingestionFlowFileSearchControllerApi;
    private final ExportFileSearchControllerApi exportFileSearchControllerApi;
    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public ProcessExecutionsApisHolder(
            @Value("${rest.process-executions.base-url}") String baseUrl,
            RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(baseUrl);
        apiClient.setBearerToken(bearerTokenHolder::get);

        this.ingestionFlowFileSearchControllerApi = new IngestionFlowFileSearchControllerApi(apiClient);
        this.exportFileSearchControllerApi = new ExportFileSearchControllerApi(apiClient);
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

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
