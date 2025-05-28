package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InstallmentRegistrySearchClient {
  private final RegistriesApisHolder registriesApisHolder;

  public InstallmentRegistrySearchClient(RegistriesApisHolder registriesApisHolder) {
    this.registriesApisHolder = registriesApisHolder;
  }

  public CollectionModelInstallmentRegistry getInstallmentRegistries(Long debtPositionId, String accessToken) {
    return registriesApisHolder.getInstallmentRegistrySearchControllerApi(accessToken)
      .crudInstallmentRegistriesFindAllByDebtPositionId(debtPositionId);
  }
}
