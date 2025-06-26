package it.gov.pagopa.pu.bff.service.sil_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.connector.registries.SilRegistryService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.springframework.stereotype.Service;

@Service
public class SilRegistryRetrieverServiceImpl implements SilRegistryRetrieverService {
  private final AuthorizationService authorizationService;
  private final OrganizationService organizationService;
  private final SilRegistryService silRegistryService;

  public SilRegistryRetrieverServiceImpl(AuthorizationService authorizationService, OrganizationService organizationService, SilRegistryService silRegistryService) {
    this.authorizationService = authorizationService;
    this.organizationService = organizationService;
    this.silRegistryService = silRegistryService;
  }

  @Override
  public SilRegistryDTO getSilRegistry(Long organizationId, String registryId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);

    String orgFiscalCode = getOrgFiscalCode(organizationId, loggedUser, accessToken);

    SilRegistryDTO silRegistry = silRegistryService.getSilRegistry(registryId, accessToken);

    if (silRegistry == null || !orgFiscalCode.equals(silRegistry.getOrgFiscalCode())) {
      throw new ResourceNotFoundException("SilRegistry with ID " + registryId + " not found or fiscal code mismatch.");
    }
    return silRegistry;
  }

  private String getOrgFiscalCode(Long organizationId, UserInfo loggedUser, String accessToken) {
    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if (organization != null && organization.getBrokerId() != null && organization.getBrokerId().equals(loggedUser.getBrokerId())) {
      return organization.getOrgFiscalCode();
    } else {
      throw new ResourceNotFoundException("Organization having organizationId " + organizationId + " not found or broker mismatch");
    }
  }
}
