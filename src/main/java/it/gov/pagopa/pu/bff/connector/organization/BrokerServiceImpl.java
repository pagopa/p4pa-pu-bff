package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.BrokerEntityClient;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.pu.bff.config.CacheConfig.Fields.broker)
public class BrokerServiceImpl implements BrokerService {

  private final BrokerEntityClient client;

  public BrokerServiceImpl(BrokerEntityClient client) {
    this.client = client;
  }

  @Override
  @Cacheable(key = "#id", unless="#result == null")
  public Broker getBrokerById(Long id, String accessToken) {
    return client.getBrokerById(id, accessToken);
  }
}
