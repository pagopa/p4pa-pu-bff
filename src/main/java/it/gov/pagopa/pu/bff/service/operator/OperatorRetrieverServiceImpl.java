package it.gov.pagopa.pu.bff.service.operator;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationOperatorMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OperatorRetrieverServiceImpl implements OperatorRetrieverService {

  private final AuthorizationService authorizationService;
  private final AuthzService authzService;
  private final DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService;
  private final PagedOrganizationOperatorMapper pagedOrganizationOperatorMapper;

    public OperatorRetrieverServiceImpl(AuthorizationService authorizationService, AuthzService authzService, DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsService, PagedOrganizationOperatorMapper pagedOrganizationOperatorMapper) {
        this.authorizationService = authorizationService;
        this.authzService = authzService;
        this.debtPositionTypeOrgOperatorsService = debtPositionTypeOrgOperatorsService;
        this.pagedOrganizationOperatorMapper = pagedOrganizationOperatorMapper;
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
}
