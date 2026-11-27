package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BrokerEntityClient {

  private final OrganizationApisHolder organizationApisHolder;

  public BrokerEntityClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public Broker getBrokerById(Long id, String accessToken) {
    try {
      return organizationApisHolder.getBrokerEntityControllerApi(accessToken)
        .crudGetBroker(String.valueOf(id));
    } catch (RestInvokeNotFoundException e) {
      log.warn("Broker with id {} not found", id);
      return null;
    }
  }

}
