package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.bff.config.RestTemplateConfig;
import it.gov.pagopa.pu.debtpositions.controller.ApiClient;
import it.gov.pagopa.pu.debtpositions.controller.BaseApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.*;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DebtPositionApisHolder {

  private final DebtPositionTypeEntityControllerApi debtPositionTypeEntityControllerApi;
  private final DebtPositionTypeWithCountSearchControllerApi debtPositionTypeWithCountSearchControllerApi;
  private final DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApi;
  private final ReceiptViewSearchControllerApi receiptViewSearchControllerApi;
  private final InstallmentViewSearchControllerApi installmentViewSearchControllerApi;
  private final ReceiptApi receiptApi;
  private final InstallmentApi installmentApi;
  private final DebtPositionViewSearchControllerApi debtPositionViewSearchControllerApi;
  private final DebtPositionApi debtPositionApi;
  private final DebtPositionTypeOrgEntityControllerApi debtPositionTypeOrgEntityControllerApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public DebtPositionApisHolder(
    DebtPositionApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    if (clientConfig.isPrintBodyWhenError()) {
      restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("DEBT-POSITIONS"));
    }

    this.debtPositionTypeEntityControllerApi = new DebtPositionTypeEntityControllerApi(apiClient);
    this.debtPositionTypeWithCountSearchControllerApi = new DebtPositionTypeWithCountSearchControllerApi(apiClient);
    this.debtPositionTypeOrgSearchControllerApi = new DebtPositionTypeOrgSearchControllerApi(apiClient);
    this.receiptViewSearchControllerApi = new ReceiptViewSearchControllerApi(apiClient);
    this.installmentViewSearchControllerApi = new InstallmentViewSearchControllerApi(apiClient);
    this.receiptApi = new ReceiptApi(apiClient);
    this.installmentApi = new InstallmentApi(apiClient);
    this.debtPositionViewSearchControllerApi = new DebtPositionViewSearchControllerApi(apiClient);
    this.debtPositionApi = new DebtPositionApi(apiClient);
    this.debtPositionTypeOrgEntityControllerApi = new DebtPositionTypeOrgEntityControllerApi(apiClient);
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

  /**
   * It will return a {@link DebtPositionTypeWithCountSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeWithCountSearchControllerApi getDebtPositionTypeWithCountSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeWithCountSearchControllerApi);
  }

  public DebtPositionTypeOrgSearchControllerApi getDebtPositionTypeOrgSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgSearchControllerApi);
  }

  public ReceiptViewSearchControllerApi getReceiptViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, receiptViewSearchControllerApi);
  }

  public InstallmentViewSearchControllerApi getInstallmentViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, installmentViewSearchControllerApi);
  }

  /**
   * It will return a {@link ReceiptApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public ReceiptApi getReceiptApi(String accessToken) {
    return getApi(accessToken, receiptApi);
  }

  /**
   * It will return a {@link InstallmentApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public InstallmentApi getInstallmentApi(String accessToken) {
    return getApi(accessToken, installmentApi);
  }

  /**
   * It will return a {@link DebtPositionViewSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionViewSearchControllerApi getDebtPositionViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionViewSearchControllerApi);
  }

  /**
   * It will return a {@link DebtPositionApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionApi getDebtPositionApi(String accessToken) {
    return getApi(accessToken, debtPositionApi);
  }

  /**
   * It will return a {@link DebtPositionTypeOrgEntityControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeOrgEntityControllerApi getDebtPositionTypeOrgEntityControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgEntityControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }

}
