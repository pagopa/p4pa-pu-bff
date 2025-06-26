package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.SilRegistryClient;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.springframework.stereotype.Service;

@Service
public class SilRegistryServiceImpl implements SilRegistryService {
  private final SilRegistryClient silRegistryClient;

  public SilRegistryServiceImpl(SilRegistryClient silRegistryClient) {
    this.silRegistryClient = silRegistryClient;
  }

  @Override
  public SilRegistryDTO getSilRegistry(String registryId, String accessToken) {
    return silRegistryClient.getSilRegistry(registryId, accessToken);
  }
}
