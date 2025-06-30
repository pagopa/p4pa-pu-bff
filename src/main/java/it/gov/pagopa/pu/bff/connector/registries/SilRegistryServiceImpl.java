package it.gov.pagopa.pu.bff.connector.registries;

import it.gov.pagopa.pu.bff.connector.registries.client.SilRegistryClient;
import it.gov.pagopa.pu.bff.connector.registries.client.SilRegistrySearchClient;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SilRegistryServiceImpl implements SilRegistryService {
  private final SilRegistryClient silRegistryClient;
  private final SilRegistrySearchClient silRegistrySearchClient;

  public SilRegistryServiceImpl(SilRegistryClient silRegistryClient, SilRegistrySearchClient silRegistrySearchClient) {
    this.silRegistryClient = silRegistryClient;
    this.silRegistrySearchClient = silRegistrySearchClient;
  }

  @Override
  public SilRegistryDTO getSilRegistry(String registryId, String accessToken) {
    return silRegistryClient.getSilRegistry(registryId, accessToken);
  }

  @Override
  public PagedModelSilRegistry searchByFilters(String orgFiscalCode, SilRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return silRegistrySearchClient.searchByFilters(orgFiscalCode,filters,pageable,accessToken);
  }
}
