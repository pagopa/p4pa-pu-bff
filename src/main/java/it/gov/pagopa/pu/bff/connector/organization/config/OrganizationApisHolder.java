package it.gov.pagopa.pu.bff.connector.organization.config;

import it.gov.pagopa.pu.bff.config.RestTemplateConfig;
import it.gov.pagopa.pu.organization.controller.ApiClient;
import it.gov.pagopa.pu.organization.controller.BaseApi;
import it.gov.pagopa.pu.organization.controller.generated.*;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class OrganizationApisHolder {

    private final OrganizationSearchControllerApi organizationSearchControllerApi;
    private final BrokerEntityControllerApi brokerEntityControllerApi;
    private final TaxonomyCollectionReasonDtoSearchControllerApi taxonomyCollectionReasonDtoSearchControllerApi;
    private final TaxonomyMacroAreaCodeDtoSearchControllerApi taxonomyMacroAreaCodeDtoSearchControllerApi;
    private final TaxonomyOrganizationTypeDtoSearchControllerApi taxonomyOrganizationTypeDtoSearchControllerApi;
    private final TaxonomyServiceTypeCodeDtoSearchControllerApi taxonomyServiceTypeCodeDtoSearchControllerApi;
    private final TaxonomyCodeDtoSearchControllerApi taxonomyCodeDtoSearchControllerApi;
    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public OrganizationApisHolder(
        OrganizationClientConfig clientConfig,
        RestTemplateBuilder restTemplateBuilder
    ) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
      apiClient.setBasePath(clientConfig.getBaseUrl());
      apiClient.setBearerToken(bearerTokenHolder::get);
      apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
      apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
      if (clientConfig.isPrintBodyWhenError()) {
        restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("ORGANIZATION"));
      }

        this.organizationSearchControllerApi = new OrganizationSearchControllerApi(apiClient);
        this.brokerEntityControllerApi = new BrokerEntityControllerApi(apiClient);
        this.taxonomyCollectionReasonDtoSearchControllerApi = new TaxonomyCollectionReasonDtoSearchControllerApi(apiClient);
        this.taxonomyMacroAreaCodeDtoSearchControllerApi = new TaxonomyMacroAreaCodeDtoSearchControllerApi(apiClient);
        this.taxonomyOrganizationTypeDtoSearchControllerApi = new TaxonomyOrganizationTypeDtoSearchControllerApi(apiClient);
        this.taxonomyServiceTypeCodeDtoSearchControllerApi = new TaxonomyServiceTypeCodeDtoSearchControllerApi(apiClient);
        this.taxonomyCodeDtoSearchControllerApi = new TaxonomyCodeDtoSearchControllerApi(apiClient);
    }

    @PreDestroy
    public void unload(){
        bearerTokenHolder.remove();
    }

    /** It will return a {@link OrganizationSearchControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public OrganizationSearchControllerApi getOrganizationSearchControllerApi(String accessToken){
        return getApi(accessToken, organizationSearchControllerApi);
    }

    /** It will return a {@link BrokerEntityControllerApi} instrumented with the provided accessToken. Use null if auth is not required */
    public BrokerEntityControllerApi getBrokerEntityControllerApi(String accessToken){
        return getApi(accessToken, brokerEntityControllerApi);
    }

    public TaxonomyCollectionReasonDtoSearchControllerApi getTaxonomyEntityControllerApi(String accessToken){
      return getApi(accessToken,taxonomyCollectionReasonDtoSearchControllerApi);
    }

    public TaxonomyMacroAreaCodeDtoSearchControllerApi getMacroArea(String accessToken){
      return getApi(accessToken,taxonomyMacroAreaCodeDtoSearchControllerApi);
    }

    public TaxonomyOrganizationTypeDtoSearchControllerApi getOrganizationTypes(String accessToken){
      return getApi(accessToken,taxonomyOrganizationTypeDtoSearchControllerApi);
    }

    public TaxonomyServiceTypeCodeDtoSearchControllerApi getServiceType(String accessToken){
      return getApi(accessToken,taxonomyServiceTypeCodeDtoSearchControllerApi);
    }

    public TaxonomyCodeDtoSearchControllerApi getTaxonomyCode(String accessToken){
      return getApi(accessToken,taxonomyCodeDtoSearchControllerApi);
    }

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
