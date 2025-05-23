package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.DebtPositionRegistrySearchClient;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionRegistryServiceImpl implements DebtPositionRegistryService {

  private final DebtPositionRegistrySearchClient client;

  public DebtPositionRegistryServiceImpl(DebtPositionRegistrySearchClient client) {
    this.client = client;
  }

  @Override
  public CollectionModelDebtPositionRegistry findDebtPositionRegistries(Long debtPositionId, String accessToken) {
    return client.findDebtPositionRegistries(debtPositionId, accessToken);
  }
}
