package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SilRegistryClient {
  private final RegistriesApisHolder registriesApisHolder;

  public SilRegistryClient(RegistriesApisHolder registriesApisHolder) {
    this.registriesApisHolder = registriesApisHolder;
  }

  public SilRegistryDTO getSilRegistry(String registryId, String accessToken) {
    try {
      return registriesApisHolder.getSilRegistryApi(accessToken)
        .getSilRegistry(registryId);
    } catch (RestInvokeNotFoundException e) {
      log.info("Sil registry with ID {} not found", registryId);
      return null;
    }
  }
}
