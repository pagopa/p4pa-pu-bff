package it.gov.pagopa.pu.bff.connector.organization.config;

import it.gov.pagopa.pu.p4pa_organization.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_organization.controller.BaseApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.BrokerEntityControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyCodeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyCollectionReasonDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyMacroAreaCodeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyOrganizationTypeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyServiceTypeCodeDtoSearchControllerApi;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
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
            @Value("${rest.organization.base-url}") String baseUrl,
            RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(baseUrl);
        apiClient.setBearerToken(bearerTokenHolder::get);

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
