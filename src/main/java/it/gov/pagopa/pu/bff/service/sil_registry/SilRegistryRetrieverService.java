package it.gov.pagopa.pu.bff.service.sil_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;

public interface SilRegistryRetrieverService {
  SilRegistryDTO getSilRegistry(Long organizationId, String registryId, UserInfo loggedUser, String accessToken);
}
