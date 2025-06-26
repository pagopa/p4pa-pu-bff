package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;

public interface SilRegistryService {
  SilRegistryDTO getSilRegistry(String registryId, String accessToken);
}
