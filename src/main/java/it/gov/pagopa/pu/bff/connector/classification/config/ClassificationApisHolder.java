package it.gov.pagopa.pu.bff.connector.classification.config;

import it.gov.pagopa.pu.bff.config.RestTemplateConfig;
import it.gov.pagopa.pu.classification.controller.ApiClient;
import it.gov.pagopa.pu.classification.controller.BaseApi;
import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingSearchControllerApi;
import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingViewSearchControllerApi;
import it.gov.pagopa.pu.classification.controller.generated.TreasuryViewSearchControllerApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClassificationApisHolder {

  private final PaymentsReportingViewSearchControllerApi paymentsReportingViewSearchControllerApi;
  private final PaymentsReportingSearchControllerApi paymentsReportingSearchControllerApi;
  private final TreasuryViewSearchControllerApi treasuryViewSearchControllerApi;
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

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }

}
