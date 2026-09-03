package it.gov.pagopa.pu.bff.service.operator;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.mapper.OperatorDetailMapper;
import it.gov.pagopa.pu.bff.mapper.PagedDebtPositionTypeOrgDTOMapper;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationOperatorMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OperatorRetrieverServiceImpl implements OperatorRetrieverService {

  private final AuthorizationService authorizationService;
  private final AuthzService authzService;
  private final DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService;
  private final PagedOrganizationOperatorMapper pagedOrganizationOperatorMapper;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final OperatorDetailMapper operatorDetailMapper;
  private final DebtPositionTypeService debtPositionTypeService;
  private final PagedDebtPositionTypeOrgDTOMapper pagedDebtPositionTypeOrgDTOMapper;
  private final OrganizationService organizationService;

    public OperatorRetrieverServiceImpl(AuthorizationService authorizationService, AuthzService authzService, DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService, PagedOrganizationOperatorMapper pagedOrganizationOperatorMapper, DebtPositionTypeOrgService debtPositionTypeOrgService, OperatorDetailMapper operatorDetailMapper, DebtPositionTypeService debtPositionTypeService,
        PagedDebtPositionTypeOrgDTOMapper pagedDebtPositionTypeOrgDTOMapper, OrganizationService organizationService) {
        this.authorizationService = authorizationService;
        this.authzService = authzService;
        this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
        this.pagedOrganizationOperatorMapper = pagedOrganizationOperatorMapper;
        this.debtPositionTypeOrgService = debtPositionTypeOrgService;
        this.operatorDetailMapper = operatorDetailMapper;
        this.debtPositionTypeService = debtPositionTypeService;
        this.pagedDebtPositionTypeOrgDTOMapper = pagedDebtPositionTypeOrgDTOMapper;
        this.organizationService = organizationService;
    }

    @Override
  public PagedOrganizationOperator getOrganizationOperators(Long organizationId, String firstName, String lastName, String fiscalCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId,loggedUser);

    String orgIpaCode = getUserOrganizationIpaCode(organizationId, loggedUser);

    OperatorsPage operatorsPage = authzService.getOrganizationOperators(
            orgIpaCode, fiscalCode, firstName, lastName,
            pageable.getPageNumber(), pageable.getPageSize(), accessToken);

    if(operatorsPage == null || operatorsPage.getContent().isEmpty()){
      return pagedOrganizationOperatorMapper.mapToPagedOrganizationOperator(operatorsPage,Collections.emptyMap(), null);
    }

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);

    return pagedOrganizationOperatorMapper.mapToPagedOrganizationOperator(
            operatorsPage,
            getOperatorDptoCount(organizationId, operatorsPage, accessToken),
            organization
    );
  }

  private String getUserOrganizationIpaCode(Long organizationId, UserInfo loggedUser) {
    return loggedUser.getOrganizations().stream()
            .filter(o -> organizationId.equals(o.getOrganizationId()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .getOrganizationIpaCode();
  }

  private Map<String, Long> getOperatorDptoCount(Long organizationId, OperatorsPage operatorsPage, String accessToken) {
    Set<String> operatorIds = operatorsPage.getContent().stream().map(OperatorDTO::getMappedExternalUserId).collect(Collectors.toSet());
    List<DebtPositionTypeOrgOperatorsDptoCountView> debtPositionTypeOrgOperators = debtPositionTypeOrgOperatorsService.findByOrganizationIdAndOperatorExternalUserIds(organizationId,operatorIds, accessToken);
    return debtPositionTypeOrgOperators.stream().collect(Collectors.toMap(DebtPositionTypeOrgOperatorsDptoCountView::getOperatorExternalUserId, DebtPositionTypeOrgOperatorsDptoCountView::getDebtPositionTypeOrgCount));
  }

  @Override
  public OperatorsDetail getOperatorDetails(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO,
                                            Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(operatorDetailsFiltersDTO.getOrganizationId(), loggedUser);

    OperatorDTO organizationOperator = getOperatorDTO(operatorDetailsFiltersDTO, loggedUser, accessToken);

    PagedModelDebtPositionTypeOrg pagedDebtPositionTypeOrg = debtPositionTypeOrgService.findPagedDebtPositionTypeOrg(operatorDetailsFiltersDTO, pageable, accessToken );

    Organization organization = organizationService.getOrganizationByOrganizationId(operatorDetailsFiltersDTO.getOrganizationId(), accessToken);

    return operatorDetailMapper.map(pagedDebtPositionTypeOrg, organizationOperator,
      getDebtPositionTypes(pagedDebtPositionTypeOrg,accessToken),
      organization);
  }

  private Map<Long, DebtPositionType> getDebtPositionTypes(PagedModelDebtPositionTypeOrg pagedDebtPositionTypeOrg, String accessToken) {
    if(pagedDebtPositionTypeOrg==null || pagedDebtPositionTypeOrg.getEmbedded()==null || CollectionUtils.isEmpty(pagedDebtPositionTypeOrg.getEmbedded().getDebtPositionTypeOrgs())){
      return Collections.emptyMap();
    }
    List<DebtPositionType> debtPositionTypes = debtPositionTypeService.findByDebtPositionTypeIds(
            pagedDebtPositionTypeOrg.getEmbedded().getDebtPositionTypeOrgs().stream().map(DebtPositionTypeOrg::getDebtPositionTypeId).collect(Collectors.toSet()),
            accessToken
    );
    return debtPositionTypes.stream().collect(Collectors.toMap(DebtPositionType::getDebtPositionTypeId, Function.identity()));
  }

  private OperatorDTO getOperatorDTO(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, UserInfo loggedUser, String accessToken) {
    String userOrganizationIpaCode = getUserOrganizationIpaCode(operatorDetailsFiltersDTO.getOrganizationId(), loggedUser);
    OperatorDTO organizationOperator = authzService.getOrganizationOperator(
      userOrganizationIpaCode,
      operatorDetailsFiltersDTO.getMappedExternalUserId(),
      accessToken
    );

    if (organizationOperator == null) {
      throw new NotFoundException("OPERATOR_NOT_FOUND", "Operator not found for organization ipaCode %s and userId %s".formatted(userOrganizationIpaCode, operatorDetailsFiltersDTO.getMappedExternalUserId()));
    }
    return organizationOperator;
  }

  @Override
  public int removeDebtPositionTypeOrgFromOperator(Long organizationId, String mappedExternalUserId, Long debtPositionTypeOrgId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    return debtPositionTypeOrgOperatorsService.deleteOperators(debtPositionTypeOrgId, Set.of(mappedExternalUserId), accessToken);
  }

  @Override
  public PagedDebtPositionTypeOrgDTO getDebtPositionTypeOrgsNotEnabledForOperator(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, UserInfo loggedUser,
      String accessToken) {
    authorizationService.validateAdminRole(operatorDetailsFiltersDTO.getOrganizationId(), loggedUser);
    getOperatorDTO(operatorDetailsFiltersDTO, loggedUser, accessToken);
    PagedModelDebtPositionTypeOrg pagedDebtPositionTypeOrg = debtPositionTypeOrgService.findDebtPositionTypeOrgNotEnabledForOperator(operatorDetailsFiltersDTO, pageable, accessToken );
    return pagedDebtPositionTypeOrgDTOMapper.map(pagedDebtPositionTypeOrg,
        getDebtPositionTypes(pagedDebtPositionTypeOrg,accessToken)
    );
  }

  @Override
  public void enableDebtPositionTypeOrgsForOperator(Long organizationId, String operatorExternalUserId, Set<Long> debtPositionTypeOrgIds, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    OperatorDetailsFiltersDTO filtersDTO = new OperatorDetailsFiltersDTO(organizationId, operatorExternalUserId, null, null, null);
    getOperatorDTO(filtersDTO, loggedUser, accessToken);

    validateDebtPositionTypeOrgIds(organizationId, debtPositionTypeOrgIds, accessToken);

    debtPositionTypeOrgOperatorsService.saveDebtPositionTypeOrgOperatorsForOperator(operatorExternalUserId, debtPositionTypeOrgIds, accessToken);
  }

  private void validateDebtPositionTypeOrgIds(Long organizationId, Set<Long> debtPositionTypeOrgIds, String accessToken) {
    CollectionModelDebtPositionTypeOrg collection = debtPositionTypeOrgService.getByDebtPositionTypeOrgIdIn(debtPositionTypeOrgIds, accessToken);
    if (collection == null || collection.getEmbedded() == null || collection.getEmbedded().getDebtPositionTypeOrgs() == null) {
      throw new NotFoundException("DEBT_POSITION_TYPE_ORG_NOT_FOUND", "No debtPositionTypeOrg found for the id: " + debtPositionTypeOrgIds);
    }

    List<DebtPositionTypeOrg> foundDptos = collection.getEmbedded().getDebtPositionTypeOrgs();
    if (foundDptos.size() != debtPositionTypeOrgIds.size()) {
      throw new NotFoundException("DEBT_POSITION_TYPE_ORG_NOT_FOUND", "Some debtPositionTypeOrgIds do not exist: " + debtPositionTypeOrgIds);
    }

    boolean allMatchOrg = foundDptos.stream()
      .map(DebtPositionTypeOrg::getOrganizationId)
      .allMatch(organizationId::equals);
    if (!allMatchOrg) {
      throw new NotFoundException("DEBT_POSITION_TYPE_ORG_NOT_FOUND", "One or more DebtPositionTypeOrg do not belong to organizationId: " + organizationId);
    }
  }
}
