package it.gov.pagopa.pu.bff.service.sil_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedSilRegistry;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.springframework.data.domain.Pageable;

public interface SilRegistryRetrieverService {
  SilRegistryDTO getSilRegistry(Long organizationId, String registryId, UserInfo loggedUser, String accessToken);

  PagedSilRegistry getSilRegistries(Long organizationId, SilRegistryFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken);
}
