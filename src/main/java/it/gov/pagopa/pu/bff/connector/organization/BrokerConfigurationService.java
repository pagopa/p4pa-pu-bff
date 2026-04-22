package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.BrokerConfiguration;

public interface BrokerConfigurationService {
  BrokerConfiguration getBrokerConfiguration(Long brokerId, String accessToken);
}
