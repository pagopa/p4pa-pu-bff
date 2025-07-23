package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelSilRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SilRegistrySearchClient {
  private final RegistriesApisHolder registriesApisHolder;

  public SilRegistrySearchClient(RegistriesApisHolder registriesApisHolder) {
    this.registriesApisHolder = registriesApisHolder;
  }

  public PagedModelSilRegistry searchByFilters(String orgFiscalCode, SilRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return registriesApisHolder.getSilRegistrySearchControllerApi(accessToken)
      .crudSilRegistriesSearchByFilters(filters.getEventType(),
      filters.getEventDate().getFrom(),
      filters.getEventDate().getTo(),
      orgFiscalCode,
      filters.getIuv(),
      filters.getOutcome(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable));
  }
}
