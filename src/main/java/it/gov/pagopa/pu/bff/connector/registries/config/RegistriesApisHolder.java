package it.gov.pagopa.pu.bff.connector.registries.config;

import it.gov.pagopa.pu.bff.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.registries.generated.ApiClient;
import it.gov.pagopa.pu.registries.generated.BaseApi;
import it.gov.pagopa.pu.registries.client.generated.*;
import it.gov.pagopa.pu.registries.dto.generated.ErrorDTO;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Service
public class RegistriesApisHolder {

  private final DebtPositionRegistrySearchControllerApi debtPositionRegistrySearchControllerApi;
  private final InstallmentRegistrySearchControllerApi installmentRegistrySearchControllerApi;
  private final PagoPaRegistrySearchControllerApi pagoPaRegistrySearchControllerApi;
  private final PagoPaRegistryApi pagoPaRegistryApi;
  private final SilRegistryApi silRegistryApi;
  private final SilRegistrySearchControllerApi silRegistrySearchControllerApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public RegistriesApisHolder(
    RegistriesApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder,
    JsonMapper jsonMapper
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "REGISTRIES", clientConfig.isPrintBodyWhenError(),
      ErrorDTO.class, ErrorDTO::getCode, ErrorDTO::getMessage)
    );

    this.debtPositionRegistrySearchControllerApi = new DebtPositionRegistrySearchControllerApi(apiClient);
    this.installmentRegistrySearchControllerApi = new InstallmentRegistrySearchControllerApi(apiClient);
    this.pagoPaRegistrySearchControllerApi = new PagoPaRegistrySearchControllerApi(apiClient);
    this.silRegistryApi = new SilRegistryApi(apiClient);
    this.silRegistrySearchControllerApi = new SilRegistrySearchControllerApi(apiClient);
    this.pagoPaRegistryApi = new PagoPaRegistryApi(apiClient);
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
   * It will return a {@link PagoPaRegistrySearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public PagoPaRegistrySearchControllerApi getPagoPaRegistrySearchControllerApi(String accessToken) {
    return getApi(accessToken, pagoPaRegistrySearchControllerApi);
  }

  /**
   * It will return a {@link SilRegistrySearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public SilRegistrySearchControllerApi getSilRegistrySearchControllerApi(String accessToken) {
    return getApi(accessToken, silRegistrySearchControllerApi);
  }

  /**
   * It will return a {@link SilRegistryApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public SilRegistryApi getSilRegistryApi(String accessToken) {
    return getApi(accessToken, silRegistryApi);
  }

  /**
   * It will return a {@link PagoPaRegistryApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public PagoPaRegistryApi getPagoPaRegistryApi(String accessToken) {
    return getApi(accessToken, pagoPaRegistryApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
