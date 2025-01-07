package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelBroker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class BrokerEntityClient {

    private final OrganizationApisHolder organizationApisHolder;

    public BrokerEntityClient(OrganizationApisHolder organizationApisHolder) {
        this.organizationApisHolder = organizationApisHolder;
    }

  public EntityModelBroker getBrokerById(Long id, String accessToken) {
    try {
      return organizationApisHolder.getBrokerEntityControllerApi(accessToken)
        .getItemResourceBrokerGet(String.valueOf(id));
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("Broker with id {} not found", id);
        return null;
      }
      log.error("Error retrieving broker by id: {}", id, e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving broker by id: {}", id, e);
      throw e;
    }
  }

}
