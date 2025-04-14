package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
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
  private final DebtPositionTypeOrgWithCountSearchControllerApi debtPositionTypeOrgWithCountSearchControllerApi;
  private final DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApi;
  private final DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi debtPositionTypeOrgCountByOrganizationIdSearchControllerApi;
  private final InstallmentApi installmentApi;
  private final InstallmentViewSearchControllerApi installmentViewSearchControllerApi;
  private final InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApi;
  private final ReceiptViewSearchControllerApi receiptViewSearchControllerApi;
  private final TransferSearchControllerApi transferSearchControllerApi;
  private final ReceiptApi receiptApi;
  private final DebtPositionViewSearchControllerApi debtPositionViewSearchControllerApi;
  private final DebtPositionApi debtPositionApi;
  private final DebtPositionTypeOrgEntityControllerApi debtPositionTypeOrgEntityControllerApi;
  private final DebtPositionTypeOrgOperatorsSearchControllerApi debtPositionTypeOrgOperatorsSearchControllerApi;
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
    this.debtPositionTypeOrgWithCountSearchControllerApi = new DebtPositionTypeOrgWithCountSearchControllerApi(apiClient);
    this.debtPositionTypeOrgSearchControllerApi = new DebtPositionTypeOrgSearchControllerApi(apiClient);
    this.debtPositionTypeOrgCountByOrganizationIdSearchControllerApi = new DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(apiClient);
    this.installmentViewSearchControllerApi = new InstallmentViewSearchControllerApi(apiClient);
    this.installmentApi = new InstallmentApi(apiClient);
    this.installmentNoPiiSearchControllerApi = new InstallmentNoPiiSearchControllerApi(apiClient);
    this.receiptViewSearchControllerApi = new ReceiptViewSearchControllerApi(apiClient);
    this.transferSearchControllerApi = new TransferSearchControllerApi(apiClient);
    this.receiptApi = new ReceiptApi(apiClient);
    this.debtPositionViewSearchControllerApi = new DebtPositionViewSearchControllerApi(apiClient);
    this.debtPositionApi = new DebtPositionApi(apiClient);
    this.debtPositionTypeOrgEntityControllerApi = new DebtPositionTypeOrgEntityControllerApi(apiClient);
    this.debtPositionTypeOrgOperatorsSearchControllerApi = new DebtPositionTypeOrgOperatorsSearchControllerApi(apiClient);
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

  public DebtPositionTypeOrgWithCountSearchControllerApi getDebtPositionTypeOrgWithCountSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgWithCountSearchControllerApi);
  }

  public DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi getDebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgCountByOrganizationIdSearchControllerApi);
  }

  public TransferSearchControllerApi getTransferSearchControllerApi(String accessToken) {
    return getApi(accessToken, transferSearchControllerApi);
  }

  public InstallmentViewSearchControllerApi getInstallmentViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, installmentViewSearchControllerApi);
  }

  public InstallmentNoPiiSearchControllerApi getInstallmentNoPiiSearchControllerApi(String accessToken) {
    return getApi(accessToken, installmentNoPiiSearchControllerApi);
  }

  /**
   * It will return a {@link InstallmentApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public InstallmentApi getInstallmentApi(String accessToken) {
    return getApi(accessToken, installmentApi);
  }

  public ReceiptViewSearchControllerApi getReceiptViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, receiptViewSearchControllerApi);
  }

  /**
   * It will return a {@link ReceiptApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public ReceiptApi getReceiptApi(String accessToken) {
    return getApi(accessToken, receiptApi);
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

  /**
   * It will return a {@link DebtPositionTypeOrgOperatorsSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeOrgOperatorsSearchControllerApi getDebtPositionTypeOrgOperatorsSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgOperatorsSearchControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }

}
