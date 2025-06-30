package it.gov.pagopa.pu.bff.service.sil_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.SilRegistryService;
import it.gov.pagopa.pu.bff.dto.SilRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedSilRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.SilRegistryMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.registries.dto.generated.SilRegistryDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SilRegistryRetrieverServiceImpl implements SilRegistryRetrieverService {
  private final AuthorizationService authorizationService;
  private final SilRegistryService silRegistryService;
  private final SilRegistryMapper silRegistryMapper;
  private final OrganizationRetrieverService organizationRetrieverService;

  public SilRegistryRetrieverServiceImpl(AuthorizationService authorizationService, SilRegistryService silRegistryService, SilRegistryMapper silRegistryMapper, OrganizationRetrieverService organizationRetrieverService) {
    this.authorizationService = authorizationService;
    this.silRegistryService = silRegistryService;
    this.silRegistryMapper = silRegistryMapper;
    this.organizationRetrieverService = organizationRetrieverService;
  }

  @Override
  public SilRegistryDTO getSilRegistry(Long organizationId, String registryId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);

    String orgFiscalCode = organizationRetrieverService.getOrgFiscalCode(organizationId, loggedUser, accessToken);

    SilRegistryDTO silRegistry = silRegistryService.getSilRegistry(registryId, accessToken);

    if (silRegistry == null || !orgFiscalCode.equals(silRegistry.getOrgFiscalCode())) {
      throw new ResourceNotFoundException("SilRegistry with ID " + registryId + " not found or fiscal code mismatch.");
    }
    return silRegistry;
  }

  @Override
  public PagedSilRegistry getSilRegistries(Long organizationId, SilRegistryFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);
    validateFilters(filters);
    return silRegistryMapper.mapToPagedSilRegistry(
      silRegistryService.searchByFilters(organizationRetrieverService.getOrgFiscalCode(organizationId, loggedUser, accessToken),
        filters, pageable, accessToken));
  }

  private void validateFilters(SilRegistryFiltersDTO filters) {
    DateUtils.validateDateFilters(filters.getEventDate(), "eventDate");
    if (filters.getEventType() == null &&
      StringUtils.isBlank(filters.getIuv()) &&
      DateUtils.isNullOrInvalidOffsetDateTimeRange(filters.getEventDate().getFrom(), filters.getEventDate().getTo())) {
      throw new IllegalArgumentException("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null");
    }
  }
}
