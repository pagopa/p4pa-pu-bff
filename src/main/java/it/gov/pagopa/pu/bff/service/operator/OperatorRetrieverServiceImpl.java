package it.gov.pagopa.pu.bff.service.operator;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeService;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OperatorDetailMapper;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationOperatorMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
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

    public OperatorRetrieverServiceImpl(AuthorizationService authorizationService, AuthzService authzService, DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService, PagedOrganizationOperatorMapper pagedOrganizationOperatorMapper, DebtPositionTypeOrgService debtPositionTypeOrgService, OperatorDetailMapper operatorDetailMapper, DebtPositionTypeService debtPositionTypeService) {
        this.authorizationService = authorizationService;
        this.authzService = authzService;
        this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
        this.pagedOrganizationOperatorMapper = pagedOrganizationOperatorMapper;
        this.debtPositionTypeOrgService = debtPositionTypeOrgService;
        this.operatorDetailMapper = operatorDetailMapper;
        this.debtPositionTypeService = debtPositionTypeService;
    }

    @Override
  public PagedOrganizationOperator getOrganizationOperators(Long organizationId, String firstName, String lastName, String fiscalCode, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);

    OperatorsPage operatorsPage = authzService.getOrganizationOperators(
            getUserOrganizationIpaCode(organizationId, loggedUser), fiscalCode, firstName, lastName,
            pageable.getPageNumber(), pageable.getPageSize(), accessToken);
    if(operatorsPage == null || operatorsPage.getContent().isEmpty()){
      return pagedOrganizationOperatorMapper.mapToPagedOrganizationOperator(operatorsPage,Collections.emptyMap());
    }
    return pagedOrganizationOperatorMapper.mapToPagedOrganizationOperator(
            operatorsPage,
            getOperatorDptoCount(organizationId, operatorsPage, accessToken)
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
    authorizationService.validateOrganizationOrBrokerAdmin(operatorDetailsFiltersDTO.getOrganizationId(), loggedUser,accessToken);

    OperatorDTO organizationOperator = getOperatorDTO(operatorDetailsFiltersDTO, loggedUser, accessToken);

    PagedModelDebtPositionTypeOrg pagedDebtPositionTypeOrg = debtPositionTypeOrgService.findPagedDebtPositionTypeOrg(operatorDetailsFiltersDTO, pageable, accessToken );

    return operatorDetailMapper.map(pagedDebtPositionTypeOrg, organizationOperator,
            getDebtPositionTypes(pagedDebtPositionTypeOrg,accessToken));
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
      throw new ResourceNotFoundException("Operator not found for organization ipaCode %s and userId %s".formatted(userOrganizationIpaCode, operatorDetailsFiltersDTO.getMappedExternalUserId()));
    }
    return organizationOperator;
  }
}
