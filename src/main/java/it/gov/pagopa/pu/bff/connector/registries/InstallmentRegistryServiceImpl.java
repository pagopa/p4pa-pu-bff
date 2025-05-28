package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.InstallmentRegistrySearchClient;
import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;
import org.springframework.stereotype.Service;

@Service
public class InstallmentRegistryServiceImpl implements InstallmentRegistryService {
  private final InstallmentRegistrySearchClient installmentRegistrySearchClient;

  public InstallmentRegistryServiceImpl(InstallmentRegistrySearchClient installmentRegistrySearchClient) {
    this.installmentRegistrySearchClient = installmentRegistrySearchClient;
  }

  @Override
  public CollectionModelInstallmentRegistry getInstallmentRegistries(Long debtPositionId, String accessToken) {
    return installmentRegistrySearchClient.getInstallmentRegistries(debtPositionId, accessToken);
  }
}
