package it.gov.pagopa.pu.bff.service.pagopa_registry;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.connector.registries.PagoPaRegistryService;
import it.gov.pagopa.pu.bff.dto.PagoPaRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagoPaRegistryMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PagoPaRegistryRetrieverServiceImpl implements PagoPaRegistryRetrieverService {
    private final AuthorizationService authorizationService;
    private final OrganizationService organizationService;
    private final PagoPaRegistryService pagoPaRegistryService;
    private final PagoPaRegistryMapper pagoPaRegistryMapper;

    public PagoPaRegistryRetrieverServiceImpl(AuthorizationService authorizationService, OrganizationService organizationService, PagoPaRegistryService pagoPaRegistryService, PagoPaRegistryMapper pagoPaRegistryMapper) {
        this.authorizationService = authorizationService;
        this.organizationService = organizationService;
        this.pagoPaRegistryService = pagoPaRegistryService;
        this.pagoPaRegistryMapper = pagoPaRegistryMapper;
    }

    @Override
    public PagedPagoPaRegistry getPagoPaRegistries(Long organizationId, PagoPaRegistryFiltersDTO filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
        authorizationService.validateBrokerAdminRole(loggedUser);
        validateFilters(filters);
        return pagoPaRegistryMapper.mapToPagedPagoPaRegistry(
                pagoPaRegistryService.searchByFilters(getOrgFiscalCode(organizationId,loggedUser,accessToken),
                        filters,pageable,accessToken));
    }

    private String getOrgFiscalCode(Long organizationId, UserInfo loggedUser, String accessToken) {
        Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
        if(organization!=null && organization.getBrokerId()!=null && organization.getBrokerId().equals(loggedUser.getBrokerId())){
            return organization.getOrgFiscalCode();
        }else{
            throw new ResourceNotFoundException("Organization having organizationId "+ organizationId +" not found");
        }
    }

    private void validateFilters(PagoPaRegistryFiltersDTO filters) {
        DateUtils.validateDateFilters(filters.getEventDate(),"eventDate");
        if (filters.getEventType() == null &&
                StringUtils.isBlank(filters.getIuv()) &&
                DateUtils.isNullOrInvalidOffsetDateTimeRange(filters.getEventDate().getFrom(), filters.getEventDate().getTo())) {
            throw new IllegalArgumentException("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null");
        }
    }
}
