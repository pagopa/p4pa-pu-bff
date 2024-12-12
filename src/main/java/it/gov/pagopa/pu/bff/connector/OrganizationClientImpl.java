package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4pa_organization.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.BrokerEntityControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.model.generated.EntityModelOrganization;
import it.gov.pagopa.pu.p4pa_organization.model.generated.PagedModelEntityModelBroker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class OrganizationClientImpl implements OrganizationClient {

  private BrokerEntityControllerApi brokerEntityControllerApi;

  private OrganizationSearchControllerApi organizationSearchControllerApi;

  public OrganizationClientImpl (RestTemplateBuilder restTemplateBuilder,
    @Value("${app.organization.base-url}") String baseUrl){
    RestTemplate restTemplate = restTemplateBuilder.build();

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
    messageConverters.add(converter);

    restTemplate.setMessageConverters(messageConverters);

    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(baseUrl);
    brokerEntityControllerApi = new BrokerEntityControllerApi(apiClient);
    organizationSearchControllerApi = new OrganizationSearchControllerApi(apiClient);
  }

  public PagedModelEntityModelBroker getBrokerConfig(){
    return brokerEntityControllerApi.getCollectionResourceBrokerGet1(null,null,null);
  }

  public EntityModelBroker getBrokerById(String id) {
    try {
      return brokerEntityControllerApi.getItemResourceBrokerGet(id);
    } catch (Exception e) {
      log.info(e.getCause());
      return null;
    }
  }

  public EntityModelOrganization getOrganizationByIpaCode(String ipaCode){
    try {
      return organizationSearchControllerApi.executeSearchOrganizationGet(ipaCode);
    } catch (Exception e) {
      log.info(e.getCause());
      return null;
    }
  }

}