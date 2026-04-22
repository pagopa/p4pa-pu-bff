package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.BrokerConfigurationEntityClient;
import it.gov.pagopa.pu.organization.dto.generated.BrokerConfiguration;
import org.springframework.stereotype.Service;

@Service
public class BrokerConfigurationServiceImpl implements BrokerConfigurationService {
  private final BrokerConfigurationEntityClient brokerConfigurationEntityClient;

  public BrokerConfigurationServiceImpl(BrokerConfigurationEntityClient brokerConfigurationEntityClient) {
    this.brokerConfigurationEntityClient = brokerConfigurationEntityClient;
  }

  @Override
  public BrokerConfiguration getBrokerConfiguration(Long brokerId, String accessToken) {
    return brokerConfigurationEntityClient.getBrokerConfiguration(brokerId, accessToken);
  }
}
