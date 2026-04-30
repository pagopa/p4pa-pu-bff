package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.PagoPaRegistryClient;
import it.gov.pagopa.pu.bff.connector.registries.client.PagoPaRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagoPaRegistryServiceImpl implements PagoPaRegistryService {
  private final PagoPaRegistrySearchClient pagoPaRegistrySearchClient;
  private final PagoPaRegistryClient pagoPaRegistryClient;

  public PagoPaRegistryServiceImpl(PagoPaRegistrySearchClient pagoPaRegistrySearchClient, PagoPaRegistryClient pagoPaRegistryClient) {
      this.pagoPaRegistrySearchClient = pagoPaRegistrySearchClient;
      this.pagoPaRegistryClient = pagoPaRegistryClient;
  }

  @Override
  public PagedModelPagoPaRegistry searchByFilters(String orgFiscalCode, PagoPaRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return pagoPaRegistrySearchClient.searchByFilters(orgFiscalCode,filters,pageable,accessToken);
  }

  @Override
  public PagoPaRegistryDTO getPagoPaRegistry(String pagoPaRegistryId, String accessToken) {
    return pagoPaRegistryClient.getPagoPaRegistry(pagoPaRegistryId,accessToken);
  }
}
