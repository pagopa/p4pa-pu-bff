package it.gov.pagopa.pu.bff.connector.debtposition.config;

import it.gov.pagopa.pu.p4pa_debt_positions.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_debt_positions.controller.BaseApi;
import it.gov.pagopa.pu.p4pa_debt_positions.controller.generated.DebtPositionTypeWithCountSearchControllerApi;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class DebtPositionApisHolder {

    private final DebtPositionTypeWithCountSearchControllerApi debtPositionTypeWithCountSearchControllerApi;
    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public DebtPositionApisHolder(
            @Value("${rest.debt-position.base-url}") String baseUrl,
            RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(baseUrl);
        apiClient.setBearerToken(bearerTokenHolder::get);

        this.debtPositionTypeWithCountSearchControllerApi = new DebtPositionTypeWithCountSearchControllerApi(apiClient);
    }

    @PreDestroy
    public void unload(){
        bearerTokenHolder.remove();
    }

    /** It will return a {@link DebtPositionTypeWithCountSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public DebtPositionTypeWithCountSearchControllerApi getDebtPositionTypeWithCountSearchControllerApi(String accessToken){
        return getApi(accessToken, debtPositionTypeWithCountSearchControllerApi);
    }

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
