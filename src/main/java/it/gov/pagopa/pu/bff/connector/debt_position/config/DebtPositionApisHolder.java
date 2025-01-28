package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.debtpositions.controller.ApiClient;
import it.gov.pagopa.pu.debtpositions.controller.BaseApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.DebtPositionTypeEntityControllerApi;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class DebtPositionApisHolder {

  private final DebtPositionTypeEntityControllerApi debtPositionTypeEntityControllerApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public DebtPositionApisHolder(
    @Value("${rest.debt-positions.base-url}") String baseUrl,
    RestTemplateBuilder restTemplateBuilder) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(baseUrl);
    apiClient.setBearerToken(bearerTokenHolder::get);

    this.debtPositionTypeEntityControllerApi = new DebtPositionTypeEntityControllerApi(apiClient);
  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  /**
   * It will return a {@link DebtPositionTypeEntityControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeEntityControllerApi getDebtPositionTypeControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeEntityControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }

}
