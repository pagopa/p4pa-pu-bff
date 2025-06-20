package it.gov.pagopa.pu.bff.connector.classification.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.classification.controller.ApiClient;
import it.gov.pagopa.pu.classification.controller.BaseApi;
import it.gov.pagopa.pu.classification.controller.generated.*;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClassificationApisHolder {

  private final PaymentsReportingViewSearchControllerApi paymentsReportingViewSearchControllerApi;
  private final PaymentsReportingSearchControllerApi paymentsReportingSearchControllerApi;
  private final TreasuryViewSearchControllerApi treasuryViewSearchControllerApi;
  private final TreasurySearchControllerApi treasurySearchControllerApi;
  private final ClassificationsApi classificationsApi;
  private final AssessmentsRegistrySearchControllerApi assessmentsRegistrySearchControllerApi;
  private final AssessmentsRegistryEntityControllerApi assessmentsRegistryEntityControllerApi;
  private final AssessmentsRegistryApi assessmentsRegistryApi;
  private final AssessmentsControllerApi assessmentsControllerApi;
  private final AssessmentsDetailSearchControllerApi assessmentsDetailSearchControllerApi;
  private final AssessmentsDetailEntityControllerApi assessmentsDetailEntityControllerApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public ClassificationApisHolder(ClassificationApiClientConfig clientConfig,
                                  RestTemplateBuilder restTemplateBuilder) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    if (clientConfig.isPrintBodyWhenError()) {
      restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("CLASSIFICATION"));
    }

    this.paymentsReportingViewSearchControllerApi = new PaymentsReportingViewSearchControllerApi(apiClient);
    this.paymentsReportingSearchControllerApi = new PaymentsReportingSearchControllerApi(apiClient);
    this.treasuryViewSearchControllerApi = new TreasuryViewSearchControllerApi(apiClient);
    this.treasurySearchControllerApi = new TreasurySearchControllerApi(apiClient);
    this.classificationsApi = new ClassificationsApi(apiClient);
    this.assessmentsRegistrySearchControllerApi = new AssessmentsRegistrySearchControllerApi(apiClient);
    this.assessmentsControllerApi = new AssessmentsControllerApi(apiClient);
    this.assessmentsRegistryEntityControllerApi = new AssessmentsRegistryEntityControllerApi(apiClient);
    this.assessmentsRegistryApi = new AssessmentsRegistryApi(apiClient);
    this.assessmentsDetailSearchControllerApi = new AssessmentsDetailSearchControllerApi(apiClient);
    this.assessmentsDetailEntityControllerApi = new AssessmentsDetailEntityControllerApi(apiClient);
  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  public PaymentsReportingViewSearchControllerApi getPaymentsReportingViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, paymentsReportingViewSearchControllerApi);
  }

  public PaymentsReportingSearchControllerApi getPaymentsReportingSearchControllerApi(String accessToken) {
    return getApi(accessToken, paymentsReportingSearchControllerApi);
  }

  public TreasuryViewSearchControllerApi getTreasuryViewSearchControllerApi(String accessToken) {
    return getApi(accessToken, treasuryViewSearchControllerApi);
  }

  public TreasurySearchControllerApi getTreasurySearchControllerApi(String accessToken) {
    return getApi(accessToken, treasurySearchControllerApi);
  }

  public ClassificationsApi getClassificationsApi(String accessToken) {
    return getApi(accessToken, classificationsApi);
  }

  public AssessmentsRegistrySearchControllerApi getAssessmentsRegistrySearchControllerApi(String accessToken) {
    return getApi(accessToken, assessmentsRegistrySearchControllerApi);
  }

  public AssessmentsRegistryEntityControllerApi getAssessmentsRegistryEntityControllerApi(String accessToken) {
    return getApi(accessToken, assessmentsRegistryEntityControllerApi);
  }

  public AssessmentsRegistryApi getAssessmentsRegistryApi(String accessToken) {
    return getApi(accessToken, assessmentsRegistryApi);
  }

  public AssessmentsControllerApi getAssessmentsControllerApi(String accessToken) {
    return getApi(accessToken, assessmentsControllerApi);
  }

  public AssessmentsDetailSearchControllerApi getAssessmentsDetailSearchControllerApi(String accessToken){
    return getApi(accessToken, assessmentsDetailSearchControllerApi);
  }

  public AssessmentsDetailEntityControllerApi getAssessmentsDetailEntityControllerApi(String accessToken){
    return getApi(accessToken, assessmentsDetailEntityControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }

}
