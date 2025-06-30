package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class PagoPaRegistryClient {
  private final RegistriesApisHolder registriesApisHolder;

  public PagoPaRegistryClient(RegistriesApisHolder registriesApisHolder) {
    this.registriesApisHolder = registriesApisHolder;
  }

  public PagoPaRegistryDTO getPagoPaRegistry(String pagoPaRegistryId, String accessToken) {
    try {
      return registriesApisHolder.getPagoPaRegistryApi(accessToken)
              .getPagoPaRegistry(pagoPaRegistryId);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("PagoPaRegistry with ID {} not found", pagoPaRegistryId);
      return null;
    }
  }
}
