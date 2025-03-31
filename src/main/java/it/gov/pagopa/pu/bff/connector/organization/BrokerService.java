package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Broker;

public interface BrokerService {

  Broker getBrokerById(Long id, String accessToken);
}
