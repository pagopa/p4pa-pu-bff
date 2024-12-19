package it.gov.pagopa.pu.bff.connector;

import it.gov.pagopa.pu.p4pa_organization.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.BrokerEntityControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrganizationClientImpl implements OrganizationClient {

  private final BrokerEntityControllerApi brokerEntityControllerApi;

  private final OrganizationSearchControllerApi organizationSearchControllerApi;

  public OrganizationClientImpl(RestTemplateBuilder restTemplateBuilder,
                                @Value("${app.organization.base-url}") String baseUrl) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(baseUrl);
    brokerEntityControllerApi = new BrokerEntityControllerApi(apiClient);
    organizationSearchControllerApi = new OrganizationSearchControllerApi(apiClient);
  }

  public EntityModelBroker getBrokerById(Long id, String accessToken) {
    try {
      return brokerEntityControllerApi.getItemResourceBrokerGet(String.valueOf(id));
    } catch (Exception e) {
      log.error(String.valueOf(e.getCause()));
      return null;
    }
  }

  public EntityModelOrganization getOrganizationByIpaCode(String ipaCode, String accessToken) {
    try {
      return organizationSearchControllerApi.executeSearchOrganizationGet(ipaCode);
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("Organization with IPA code {} not found", ipaCode);
        return null;
      }
      log.error("Error retrieving organization by IPA code: {}", ipaCode, e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving organization by IPA code: {}", ipaCode, e);
      throw e;
    }
  }

}
