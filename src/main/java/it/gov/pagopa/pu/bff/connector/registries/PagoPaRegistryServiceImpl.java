package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.PagoPaRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagoPaRegistryServiceImpl implements PagoPaRegistryService {
  private final PagoPaRegistrySearchClient pagoPaRegistrySearchClient;

  public PagoPaRegistryServiceImpl(PagoPaRegistrySearchClient pagoPaRegistrySearchClient) {
      this.pagoPaRegistrySearchClient = pagoPaRegistrySearchClient;
  }

  @Override
  public PagedModelPagoPaRegistry searchByFilters(String orgFiscalCode, PagoPaRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return pagoPaRegistrySearchClient.searchByFilters(orgFiscalCode,filters,pageable,accessToken);
  }
}
