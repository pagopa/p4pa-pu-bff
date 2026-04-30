package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.springframework.data.domain.Pageable;

public interface SilRegistryService {
  SilRegistryDTO getSilRegistry(String registryId, String accessToken);

  PagedModelSilRegistry searchByFilters(String orgFiscalCode, SilRegistryFiltersDTO filters, Pageable pageable, String accessToken);
}
