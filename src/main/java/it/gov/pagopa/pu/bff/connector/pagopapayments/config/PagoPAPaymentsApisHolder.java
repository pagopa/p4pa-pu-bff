package it.gov.pagopa.pu.bff.connector.pagopapayments.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.pagopapayments.controller.ApiClient;
import it.gov.pagopa.pu.pagopapayments.controller.BaseApi;
import it.gov.pagopa.pu.pagopapayments.controller.generated.PrintPaymentNoticeApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PagoPAPaymentsApisHolder {

    private final PrintPaymentNoticeApi printPaymentNoticeApi;
    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public PagoPAPaymentsApisHolder(
        PagoPAPaymentsApiClientConfig clientConfig,
        RestTemplateBuilder restTemplateBuilder
    ) {
      RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(clientConfig.getBaseUrl());
        apiClient.setBearerToken(bearerTokenHolder::get);
        apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
        apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
        if (clientConfig.isPrintBodyWhenError()) {
          restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("PAGOPA-PAYMENTS"));
        }

        this.printPaymentNoticeApi = new PrintPaymentNoticeApi(apiClient);
    }

    @PreDestroy
    public void unload(){
        bearerTokenHolder.remove();
    }

    /** It will return a {@link PrintPaymentNoticeApi} instrumented with the provided accessToken. Use null if auth is not required */
    public PrintPaymentNoticeApi getPrintPaymentNoticeControllerApi(String accessToken){
        return getApi(accessToken, printPaymentNoticeApi);
    }

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
