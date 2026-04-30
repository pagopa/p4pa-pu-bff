package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.registries.dto.generated.CollectionModelDebtPositionRegistry;

public interface DebtPositionRegistryService {
  CollectionModelDebtPositionRegistry findDebtPositionRegistries(Long debtPositionId, String accessToken);
}
