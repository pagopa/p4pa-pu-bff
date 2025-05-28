package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.registries.dto.generated.CollectionModelInstallmentRegistry;

public interface InstallmentRegistryService {
  CollectionModelInstallmentRegistry getInstallmentRegistries(Long debtPositionId, String accessToken);
}
