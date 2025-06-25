package it.gov.pagopa.pu.bff.connector.registries.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.registries.controller.ApiClient;
import it.gov.pagopa.pu.registries.controller.BaseApi;
import it.gov.pagopa.pu.registries.controller.generated.DebtPositionRegistrySearchControllerApi;
import it.gov.pagopa.pu.registries.controller.generated.InstallmentRegistrySearchControllerApi;
import it.gov.pagopa.pu.registries.controller.generated.SilRegistryApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistriesApisHolder {

  private final DebtPositionRegistrySearchControllerApi debtPositionRegistrySearchControllerApi;
  private final InstallmentRegistrySearchControllerApi installmentRegistrySearchControllerApi;
  private final SilRegistryApi silRegistryApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public RegistriesApisHolder(
    RegistriesApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    if (clientConfig.isPrintBodyWhenError()) {
      restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("REGISTRIES"));
    }

    this.debtPositionRegistrySearchControllerApi = new DebtPositionRegistrySearchControllerApi(apiClient);
    this.installmentRegistrySearchControllerApi = new InstallmentRegistrySearchControllerApi(apiClient);
    this.silRegistryApi = new SilRegistryApi(apiClient);
  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  /**
   * It will return a {@link DebtPositionRegistrySearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionRegistrySearchControllerApi getDebtPositionRegistrySearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionRegistrySearchControllerApi);
  }

  /**
   * It will return a {@link InstallmentRegistrySearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public InstallmentRegistrySearchControllerApi getInstallmentRegistrySearchControllerApi(String accessToken) {
    return getApi(accessToken, installmentRegistrySearchControllerApi);
  }

  /**
   * It will return a {@link SilRegistryApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public SilRegistryApi getSilRegistryApi(String accessToken) {
    return getApi(accessToken, silRegistryApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
