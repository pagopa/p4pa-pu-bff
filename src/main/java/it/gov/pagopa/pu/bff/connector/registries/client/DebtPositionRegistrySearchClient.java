package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DebtPositionRegistrySearchClient {

  private final RegistriesApisHolder registriesApisHolder;

  public DebtPositionRegistrySearchClient(
    RegistriesApisHolder registriesApisHolder) {
    this.registriesApisHolder = registriesApisHolder;
  }

  public CollectionModelDebtPositionRegistry findDebtPositionRegistries(
    Long debtPositionId, String accessToken) {
    return registriesApisHolder.getDebtPositionRegistrySearchControllerApi(
        accessToken)
            .crudDebtPositionRegistriesFindAllByDebtPositionId(debtPositionId);
  }

}
