package it.gov.pagopa.pu.bff.connector.organization.config;

import it.gov.pagopa.pu.bff.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.organization.controller.ApiClient;
import it.gov.pagopa.pu.organization.controller.BaseApi;
import it.gov.pagopa.pu.organization.controller.generated.*;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrganizationApisHolder {

    private final OrganizationSearchControllerApi organizationSearchControllerApi;
    private final BrokerEntityControllerApi brokerEntityControllerApi;
    private final TaxonomyEntityControllerApi taxonomyEntityControllerApi;
    private final TaxonomySearchControllerApi taxonomySearchControllerApi;
    private final TaxonomyCollectionReasonDtoSearchControllerApi taxonomyCollectionReasonDtoSearchControllerApi;
    private final TaxonomyMacroAreaCodeDtoSearchControllerApi taxonomyMacroAreaCodeDtoSearchControllerApi;
    private final TaxonomyOrganizationTypeDtoSearchControllerApi taxonomyOrganizationTypeDtoSearchControllerApi;
    private final TaxonomyServiceTypeCodeDtoSearchControllerApi taxonomyServiceTypeCodeDtoSearchControllerApi;
    private final TaxonomyCodeDtoSearchControllerApi taxonomyCodeDtoSearchControllerApi;
    private final OrganizationEntityControllerApi organizationEntityControllerApi;
    private final OrgSilServiceSearchControllerApi orgSilServiceSearchControllerApi;
    private final OrgSilServiceEntityControllerApi orgSilServiceEntityControllerApi;
    private final OrgSilServiceViewSearchControllerApi orgSilServiceViewSearchControllerApi;
    private final OrganizationSilServiceApi organizationSilServiceApi;
    private final OrganizationApi organizationApi;
    private final BrokerConfigurationEntityControllerApi brokerConfigurationEntityControllerApi;
    private final OrgSubUnitEntityControllerApi orgSubUnitEntityControllerApi;
    private final PdndClientApi pdndClientApi;
    private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

    public OrganizationApisHolder(
        OrganizationApiClientConfig clientConfig,
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

        this.taxonomyEntityControllerApi = new TaxonomyEntityControllerApi(apiClient);
        this.organizationSearchControllerApi = new OrganizationSearchControllerApi(apiClient);
        this.brokerEntityControllerApi = new BrokerEntityControllerApi(apiClient);
        this.taxonomySearchControllerApi = new TaxonomySearchControllerApi(apiClient);
        this.taxonomyCollectionReasonDtoSearchControllerApi = new TaxonomyCollectionReasonDtoSearchControllerApi(apiClient);
        this.taxonomyMacroAreaCodeDtoSearchControllerApi = new TaxonomyMacroAreaCodeDtoSearchControllerApi(apiClient);
        this.taxonomyOrganizationTypeDtoSearchControllerApi = new TaxonomyOrganizationTypeDtoSearchControllerApi(apiClient);
        this.taxonomyServiceTypeCodeDtoSearchControllerApi = new TaxonomyServiceTypeCodeDtoSearchControllerApi(apiClient);
        this.taxonomyCodeDtoSearchControllerApi = new TaxonomyCodeDtoSearchControllerApi(apiClient);
        this.organizationEntityControllerApi = new OrganizationEntityControllerApi(apiClient);
        this.orgSilServiceSearchControllerApi = new OrgSilServiceSearchControllerApi(apiClient);
        this.orgSilServiceEntityControllerApi = new OrgSilServiceEntityControllerApi(apiClient);
        this.orgSilServiceViewSearchControllerApi = new OrgSilServiceViewSearchControllerApi(apiClient);
        this.organizationSilServiceApi = new OrganizationSilServiceApi(apiClient);
        this.organizationApi = new OrganizationApi(apiClient);
        this.brokerConfigurationEntityControllerApi = new BrokerConfigurationEntityControllerApi(apiClient);
        this.orgSubUnitEntityControllerApi = new OrgSubUnitEntityControllerApi(apiClient);
        this.pdndClientApi = new PdndClientApi(apiClient);
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

    public TaxonomyEntityControllerApi getTaxonomy(String accessToken) {
      return getApi(accessToken, taxonomyEntityControllerApi);
    }

    public TaxonomySearchControllerApi getTaxonomySearchControllerApi(String accessToken){
      return getApi(accessToken,taxonomySearchControllerApi);
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

    public OrganizationEntityControllerApi getOrganizationEntityControllerApi(String accessToken){
      return getApi(accessToken,organizationEntityControllerApi);
    }

    public OrgSilServiceSearchControllerApi getOrgSilServiceSearchControllerApi(String accessToken){
      return getApi(accessToken,orgSilServiceSearchControllerApi);
    }

    public OrgSilServiceEntityControllerApi getOrgSilServiceEntityControllerApi(String accessToken){
      return getApi(accessToken, orgSilServiceEntityControllerApi);
    }

    public OrgSilServiceViewSearchControllerApi getOrgSilServiceViewSearchControllerApi(String accessToken) {
      return getApi(accessToken, orgSilServiceViewSearchControllerApi);
    }

    public OrganizationSilServiceApi getOrganizationSilServiceApi(String accessToken){
      return getApi(accessToken, organizationSilServiceApi);
    }

    public OrganizationApi getOrganizationApi(String accessToken){
      return getApi(accessToken, organizationApi);
    }

    public BrokerConfigurationEntityControllerApi getBrokerConfigurationEntityControllerApi(String accessToken){
      return getApi(accessToken, brokerConfigurationEntityControllerApi);
    }

    public OrgSubUnitEntityControllerApi getOrgSubUnitEntityControllerApi(String accessToken) {
      return getApi(accessToken, orgSubUnitEntityControllerApi);
    }

    public PdndClientApi getPdndClientApi(String accessToken) {
      return getApi(accessToken, pdndClientApi);
    }

    private <T extends BaseApi> T getApi(String accessToken, T api) {
        bearerTokenHolder.set(accessToken);
        return api;
    }
}
