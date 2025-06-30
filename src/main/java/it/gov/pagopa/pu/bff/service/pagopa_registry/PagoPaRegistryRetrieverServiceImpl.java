package it.gov.pagopa.pu.bff.service.pagopa_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.registries.PagoPaRegistryService;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagoPaRegistryMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.organization.OrganizationRetrieverService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.registries.dto.generated.PagoPaRegistryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PagoPaRegistryRetrieverServiceImpl implements PagoPaRegistryRetrieverService {
    private final AuthorizationService authorizationService;
    private final OrganizationRetrieverService organizationRetrieverService;
    private final PagoPaRegistryService pagoPaRegistryService;
    private final PagoPaRegistryMapper pagoPaRegistryMapper;

    public PagoPaRegistryRetrieverServiceImpl(AuthorizationService authorizationService, OrganizationRetrieverService organizationRetrieverService, PagoPaRegistryService pagoPaRegistryService, PagoPaRegistryMapper pagoPaRegistryMapper) {
        this.authorizationService = authorizationService;
        this.organizationRetrieverService = organizationRetrieverService;
        this.pagoPaRegistryService = pagoPaRegistryService;
        this.pagoPaRegistryMapper = pagoPaRegistryMapper;
    }

    @Override
    public PagedPagoPaRegistry getPagoPaRegistries(Long organizationId, PagoPaRegistryFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
        authorizationService.validateBrokerAdminRole(loggedUser);
        validateFilters(filters);
        return pagoPaRegistryMapper.mapToPagedPagoPaRegistry(
                pagoPaRegistryService.searchByFilters(organizationRetrieverService.getOrgFiscalCode(organizationId,loggedUser,accessToken),
                        filters,pageable,accessToken));
    }

    private void validateFilters(PagoPaRegistryFiltersDTO filters) {
        DateUtils.validateDateFilters(filters.getEventDate(),"eventDate");
        if (filters.getEventType() == null &&
                StringUtils.isBlank(filters.getIuv()) &&
                DateUtils.isNullOrInvalidOffsetDateTimeRange(filters.getEventDate().getFrom(), filters.getEventDate().getTo())) {
            throw new IllegalArgumentException("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null");
        }
    }

    @Override
    public PagoPaRegistryDTO getPagoPaRegistry(Long organizationId, String pagoPaRegistryId, UserInfo loggedUser, String accessToken) {
        authorizationService.validateBrokerAdminRole(loggedUser);
        PagoPaRegistryDTO pagoPaRegistry = pagoPaRegistryService.getPagoPaRegistry(pagoPaRegistryId, accessToken);
        if(pagoPaRegistry!=null){
            validatePagoPaRegistry(organizationId, pagoPaRegistry, loggedUser, accessToken);
        }
        return pagoPaRegistry;
    }

    private void validatePagoPaRegistry(Long organizationId, PagoPaRegistryDTO pagoPaRegistryDTO, UserInfo loggedUser, String accessToken) {
        String orgFiscalCode = organizationRetrieverService.getOrgFiscalCode(organizationId, loggedUser, accessToken);
        if (StringUtils.isBlank(orgFiscalCode) || !orgFiscalCode.equals(pagoPaRegistryDTO.getOrgFiscalCode())) {
            throw new ResourceNotFoundException("PagoPaRegistry having id " + pagoPaRegistryDTO.getRegistryId() + " not found");
        }
    }
}
