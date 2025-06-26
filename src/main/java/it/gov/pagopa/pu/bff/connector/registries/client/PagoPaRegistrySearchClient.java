package it.gov.pagopa.pu.bff.connector.registries.client;

import it.gov.pagopa.pu.bff.connector.registries.config.RegistriesApisHolder;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PagoPaRegistrySearchClient {
  private final RegistriesApisHolder registriesApisHolder;

  public PagoPaRegistrySearchClient(RegistriesApisHolder registriesApisHolder) {
    this.registriesApisHolder = registriesApisHolder;
  }

  public PagedModelPagoPaRegistry searchByFilters(String orgFiscalCode, PagoPaRegistryFiltersDTO filters, Pageable pageable, String accessToken) {
    return registriesApisHolder.getPagoPaRegistrySearchControllerApi(accessToken)
      .crudPagopaRegistriesSearchByFilters(filters.getEventType(),
        filters.getEventDate().getFrom(),
        filters.getEventDate().getTo(),
        orgFiscalCode,
        filters.getIuv(),
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }
}
