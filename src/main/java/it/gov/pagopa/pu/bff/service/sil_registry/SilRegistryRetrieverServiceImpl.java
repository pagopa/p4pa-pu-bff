package it.gov.pagopa.pu.bff.service.sil_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.SilRegistryService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.springframework.stereotype.Service;

@Service
public class SilRegistryRetrieverServiceImpl implements SilRegistryRetrieverService {
  private final AuthorizationService authorizationService;
  private final SilRegistryService silRegistryService;

  public SilRegistryRetrieverServiceImpl(AuthorizationService authorizationService, SilRegistryService silRegistryService) {
    this.authorizationService = authorizationService;
    this.silRegistryService = silRegistryService;
  }

  @Override
  public SilRegistryDTO getSilRegistry(String registryId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);
    return silRegistryService.getSilRegistry(registryId, accessToken);
  }
}
