package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.bff.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.bff.connector.debt_position.mapper.DebtPositionErrorDTOMapper;
import it.gov.pagopa.pu.debtpositions.generated.ApiClient;
import it.gov.pagopa.pu.debtpositions.generated.BaseApi;
import it.gov.pagopa.pu.debtpositions.client.generated.*;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionErrorDTO;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Service
public class DebtPositionApisHolder {

  private final DebtPositionTypeEntityControllerApi debtPositionTypeEntityControllerApi;
  private final DebtPositionTypeWithCountSearchControllerApi debtPositionTypeWithCountSearchControllerApi;
  private final DebtPositionTypeOrgWithCountSearchControllerApi debtPositionTypeOrgWithCountSearchControllerApi;
  private final DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApi;
  private final DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi debtPositionTypeOrgCountByOrganizationIdSearchControllerApi;
  private final InstallmentApi installmentApi;
  private final InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApi;
  private final ReceiptViewSearchControllerApi receiptViewSearchControllerApi;
  private final TransferApi transferApi;
  private final TransferSearchControllerApi transferSearchControllerApi;
  private final ReceiptApi receiptApi;
  private final DebtPositionViewSearchControllerApi debtPositionViewSearchControllerApi;
  private final DebtPositionApi debtPositionApi;
  private final DebtPositionTypeOrgEntityControllerApi debtPositionTypeOrgEntityControllerApi;
  private final DebtPositionSearchControllerApi debtPositionSearchControllerApi;
  private final DebtPositionTypeOrgApi debtPositionTypeOrgApi;
  private final DebtPositionTypeOrgOperatorsSearchControllerApi debtPositionTypeOrgOperatorsSearchControllerApi;
  private final DebtPositionTypeSearchControllerApi debtPositionTypeSearchControllerApi;
  private final DebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi;
  private final DebtPositionTypeOrgOperatorsApi debtPositionTypeOrgOperatorsApi;
  private final SpontaneousFormSearchControllerApi spontaneousFormSearchControllerApi;
  private final SpontaneousFormEntityControllerApi spontaneousFormEntityControllerApi;
  private final SpontaneousFormApi spontaneousFormApi;
  private final DebtPositionTypeOrgBalanceCostSearchControllerApi debtPositionTypeOrgBalanceCostSearchControllerApi;

  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public DebtPositionApisHolder(
    DebtPositionApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder,
    JsonMapper jsonMapper
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "DEBT-POSITIONS", clientConfig.isPrintBodyWhenError(),
      DebtPositionErrorDTO.class, DebtPositionErrorDTOMapper::map)
    );

    this.debtPositionTypeEntityControllerApi = new DebtPositionTypeEntityControllerApi(apiClient);
    this.debtPositionTypeWithCountSearchControllerApi = new DebtPositionTypeWithCountSearchControllerApi(apiClient);
    this.debtPositionTypeOrgWithCountSearchControllerApi = new DebtPositionTypeOrgWithCountSearchControllerApi(apiClient);
    this.debtPositionTypeOrgSearchControllerApi = new DebtPositionTypeOrgSearchControllerApi(apiClient);
    this.debtPositionTypeOrgCountByOrganizationIdSearchControllerApi = new DebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(apiClient);
    this.installmentApi = new InstallmentApi(apiClient);
    this.installmentNoPiiSearchControllerApi = new InstallmentNoPiiSearchControllerApi(apiClient);
    this.receiptViewSearchControllerApi = new ReceiptViewSearchControllerApi(apiClient);
    this.transferApi = new TransferApi(apiClient);
    this.transferSearchControllerApi = new TransferSearchControllerApi(apiClient);
    this.receiptApi = new ReceiptApi(apiClient);
    this.debtPositionViewSearchControllerApi = new DebtPositionViewSearchControllerApi(apiClient);
    this.debtPositionApi = new DebtPositionApi(apiClient);
    this.debtPositionTypeOrgEntityControllerApi = new DebtPositionTypeOrgEntityControllerApi(apiClient);
    this.debtPositionSearchControllerApi = new DebtPositionSearchControllerApi(apiClient);
    this.debtPositionTypeOrgApi = new DebtPositionTypeOrgApi(apiClient);
    this.debtPositionTypeOrgOperatorsSearchControllerApi = new DebtPositionTypeOrgOperatorsSearchControllerApi(apiClient);
    this.debtPositionTypeSearchControllerApi = new DebtPositionTypeSearchControllerApi(apiClient);
    this.debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi = new DebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(apiClient);
    this.debtPositionTypeOrgOperatorsApi = new DebtPositionTypeOrgOperatorsApi(apiClient);
    this.spontaneousFormSearchControllerApi = new SpontaneousFormSearchControllerApi(apiClient);
    this.spontaneousFormEntityControllerApi = new SpontaneousFormEntityControllerApi(apiClient);
    this.spontaneousFormApi = new SpontaneousFormApi(apiClient);
    this.debtPositionTypeOrgBalanceCostSearchControllerApi = new DebtPositionTypeOrgBalanceCostSearchControllerApi(apiClient);
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

  public TransferApi getTransferApi(String accessToken) {
    return getApi(accessToken, transferApi);
  }

  public TransferSearchControllerApi getTransferSearchControllerApi(String accessToken) {
    return getApi(accessToken, transferSearchControllerApi);
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
   * It will return a {@link DebtPositionSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionSearchControllerApi getDebtPositionSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionSearchControllerApi);
  }

  /**
   * It will return a {@link DebtPositionTypeOrgApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeOrgApi getDebtPositionTypeOrgApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgApi);
  }

  /**
   * It will return a {@link DebtPositionTypeOrgOperatorsSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeOrgOperatorsSearchControllerApi getDebtPositionTypeOrgOperatorsSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgOperatorsSearchControllerApi);
  }

  /**
   * It will return a {@link DebtPositionTypeSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeSearchControllerApi getDebtPositionTypeSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeSearchControllerApi);
  }

  /**
   * It will return a {@link DebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi);
  }

  public DebtPositionTypeOrgOperatorsApi getDebtPositionTypeOrgOperatorsApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgOperatorsApi);
  }

  /**
   * It will return a {@link SpontaneousFormSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public SpontaneousFormSearchControllerApi getSpontaneousFormSearchControllerApi(String accessToken) {
    return getApi(accessToken, spontaneousFormSearchControllerApi);
  }

  /**
   * It will return a {@link SpontaneousFormEntityControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public SpontaneousFormEntityControllerApi getSpontaneousFormEntityControllerApi(String accessToken) {
    return getApi(accessToken, spontaneousFormEntityControllerApi);
  }

  /**
   * It will return a {@link SpontaneousFormApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public SpontaneousFormApi getSpontaneousFormApi(String accessToken) {
    return getApi(accessToken, spontaneousFormApi);
  }

  /**
   * It will return a {@link DebtPositionTypeOrgBalanceCostSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required
   */
  public DebtPositionTypeOrgBalanceCostSearchControllerApi getDebtPositionTypeOrgBalanceCostSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgBalanceCostSearchControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }

}
