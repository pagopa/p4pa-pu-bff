package it.gov.pagopa.pu.bff.service.pagopa_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import org.springframework.data.domain.Pageable;

public interface PagoPaRegistryRetrieverService {
    PagedPagoPaRegistry getPagoPaRegistries(Long organizationId, PagoPaRegistryFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken);
}
