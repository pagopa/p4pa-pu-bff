package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import org.springframework.data.domain.Pageable;

public interface PagoPaRegistryService {
  PagedModelPagoPaRegistry searchByFilters(String orgFiscalCode,PagoPaRegistryFiltersDTO filters, Pageable pageable, String accessToken);
  PagoPaRegistryDTO getPagoPaRegistry(String pagoPaRegistryId, String accessToken);
}
