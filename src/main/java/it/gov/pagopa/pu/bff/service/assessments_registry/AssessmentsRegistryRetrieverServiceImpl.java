package it.gov.pagopa.pu.bff.service.assessments_registry;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsRegistryService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.AssessmentsRegistryMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssessmentsRegistryRetrieverServiceImpl implements AssessmentsRegistryRetrieverService{
    private final DebtPositionTypeOrgService debtPositionTypeOrgService;
    private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;
    private final AssessmentsRegistryService assessmentsRegistryService;
    private final AssessmentsRegistryMapper assessmentsRegistryMapper;

    public AssessmentsRegistryRetrieverServiceImpl(DebtPositionTypeOrgService debtPositionTypeOrgService, DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService, AssessmentsRegistryService assessmentsRegistryService, AssessmentsRegistryMapper assessmentsRegistryMapper) {
        this.debtPositionTypeOrgService = debtPositionTypeOrgService;
        this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
        this.assessmentsRegistryService = assessmentsRegistryService;
        this.assessmentsRegistryMapper = assessmentsRegistryMapper;
    }

    @Override
    public PagedAssessmentsRegistry getAssessmentsRegistries(AssessmentsRegistryFiltersDTO filters, String debtPositionTypeOrgCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
        AuthorizationService.validateUserForOrganizationId(filters.getOrganizationId(), loggedUser);

        if (StringUtils.isNotBlank(debtPositionTypeOrgCode)){
            debtPositionTypeOrgRetrieverService.validateOperator(filters.getOrganizationId(), debtPositionTypeOrgCode, loggedUser.getMappedExternalUserId(), accessToken);
            filters.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
        }else{
            filters.setDebtPositionTypeOrgCodes(getDebtPositionTypeOrgCodes(filters.getOrganizationId(),loggedUser.getMappedExternalUserId(),accessToken));
        }
        return assessmentsRegistryMapper.mapToPagedAssessmentsRegistry(
                assessmentsRegistryService.findAssessmentsRegistriesByFilters(filters,pageable,accessToken));
    }

    private Set<String> getDebtPositionTypeOrgCodes(Long organizationId, String mappedExternalUserId, String accessToken) {
        CollectionModelDebtPositionTypeOrg debtPositionTypeOrgs = debtPositionTypeOrgService.getDebtPositionTypeOrgs(organizationId, mappedExternalUserId, accessToken);
        if(debtPositionTypeOrgs!=null
                && debtPositionTypeOrgs.getEmbedded() != null
                && !CollectionUtils.isEmpty(debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs())){
            return debtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs().stream().map(DebtPositionTypeOrg::getCode).collect(Collectors.toSet());
        }else{
            throw new ResourceNotFoundException("AssessmentsRegistries not found for organizationId "+organizationId);
        }
    }
}
