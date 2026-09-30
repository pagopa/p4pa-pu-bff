package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitOperatorsService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.OrgSubUnitOperatorsFilters;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.mapper.PagedOrgSubUnitOperatorsMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperatorsEmbedded;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrgSubUnitOperatorsRetrieverServiceImpl implements OrgSubUnitOperatorsRetrieverService {

  private final AuthorizationService authorizationService;
  private final OrgSubUnitOperatorsService orgSubUnitOperatorsService;
  private final PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapper;
  private final AuthzService authzService;
  private final OrganizationService organizationService;
  private final Integer pageMaxSize;

  public OrgSubUnitOperatorsRetrieverServiceImpl(
    AuthorizationService authorizationService,
    OrgSubUnitOperatorsService orgSubUnitOperatorsService,
    PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapper,
    AuthzService authzService,
    OrganizationService organizationService,
    @Value("${rest.page.request-max-page-size}") Integer pageMaxSize) {
    this.authorizationService = authorizationService;
    this.orgSubUnitOperatorsService = orgSubUnitOperatorsService;
    this.pagedOrgSubUnitOperatorsMapper = pagedOrgSubUnitOperatorsMapper;
    this.authzService = authzService;
    this.organizationService = organizationService;
    this.pageMaxSize = pageMaxSize;
  }

  @Override
  public PagedOrgSubUnitOperators getOrgSubUnitOperators(Long organizationId, String subUnitCode, OrgSubUnitOperatorsFilters filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);

    String organizationIpaCode = organization.getIpaCode();

    Map<String, OperatorDTO> operatorsMap;
    PagedModelOrgSubUnitOperators pagedModelOrgSubUnitOperators;

    if (hasAuthzFilters(filters)) {
      operatorsMap = retrieveOperatorsMap(organizationIpaCode, filters.getMappedExternalUserId(), filters.getFiscalCode(), filters.getFirstName(), filters.getLastName(), accessToken);
      pagedModelOrgSubUnitOperators = orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(organizationId, subUnitCode, operatorsMap.keySet(), pageable, accessToken);
    } else if (StringUtils.hasText(filters.getMappedExternalUserId())) {
      pagedModelOrgSubUnitOperators = orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(organizationId, subUnitCode, Set.of(filters.getMappedExternalUserId()), pageable, accessToken);
      operatorsMap = Collections.emptyMap();
    } else {
      pagedModelOrgSubUnitOperators = orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, pageable, accessToken);
      operatorsMap = Collections.emptyMap();
    }

    List<OrgSubUnitOperators> orgSubUnitOperators = extractOperators(pagedModelOrgSubUnitOperators);

    List<OrgSubUnitOperator> content = orgSubUnitOperators.stream()
      .map(operator -> enrichWithOperatorInfo(operator, operatorsMap))
      .toList();

    return pagedOrgSubUnitOperatorsMapper.map(content, pagedModelOrgSubUnitOperators);
  }

  private List<OrgSubUnitOperators> extractOperators(PagedModelOrgSubUnitOperators paged) {
    return Optional.ofNullable(paged.getEmbedded())
      .map(PagedModelOrgSubUnitOperatorsEmbedded::getOrgSubUnitOperatorses)
      .orElse(Collections.emptyList());
  }

  private Map<String, OperatorDTO> retrieveOperatorsMap(String organizationIpaCode, String mappedExternalUserId, String fiscalCode, String firstName, String lastName, String accessToken) {
    if (StringUtils.hasText(mappedExternalUserId)) {
      return retrieveSingleOperator(organizationIpaCode, mappedExternalUserId, fiscalCode, firstName, lastName, accessToken);
    }
    return retrieveOperators(organizationIpaCode, fiscalCode, firstName, lastName, accessToken);
  }

  private Map<String, OperatorDTO> retrieveSingleOperator(String organizationIpaCode, String mappedExternalUserId, String fiscalCode, String firstName, String lastName, String accessToken) {
    OperatorDTO operator = authzService.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken);

    if (!matchesFilters(operator, fiscalCode, firstName, lastName)) {
      return Collections.emptyMap();
    }

    return Map.of(operator.getMappedExternalUserId(), operator);
  }

  private Map<String, OperatorDTO> retrieveOperators(String organizationIpaCode, String fiscalCode, String firstName, String lastName, String accessToken) {
    OperatorsPage operatorsPage = authzService.getOrganizationOperators(organizationIpaCode, fiscalCode, firstName, lastName, 0, pageMaxSize, accessToken);

    return operatorsPage.getContent().stream()
      .collect(Collectors.toMap(OperatorDTO::getMappedExternalUserId, Function.identity()));
  }

  private boolean matchesFilters(OperatorDTO operator, String fiscalCode, String firstName, String lastName) {
    return matches(fiscalCode, operator.getFiscalCode())
      && matches(firstName, operator.getFirstName())
      && matches(lastName, operator.getLastName());
  }

  private boolean matches(String filter, String value) {
    return !StringUtils.hasText(filter)
      || (value != null && value.equalsIgnoreCase(filter));
  }

  private OrgSubUnitOperator enrichWithOperatorInfo(OrgSubUnitOperators sourceOperator, Map<String, OperatorDTO> operatorsMap) {
    OperatorDTO operatorDTO = operatorsMap.get(sourceOperator.getOperatorExternalUserId());
    return pagedOrgSubUnitOperatorsMapper.toOrgSubUnitOperator(sourceOperator, operatorDTO);
  }

  private boolean hasAuthzFilters(OrgSubUnitOperatorsFilters filters) {
    return StringUtils.hasText(filters.getFiscalCode())
      || StringUtils.hasText(filters.getFirstName())
      || StringUtils.hasText(filters.getLastName());
  }

  @Override
  public void addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    orgSubUnitOperatorsService.addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes, accessToken);
  }

  @Override
  public void deleteOrgSubUnitFromOperator(Long organizationId, String mappedExternalUserId, String subUnitCode, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    orgSubUnitOperatorsService.deleteOrgSubUnitFromOperator(organizationId, mappedExternalUserId, subUnitCode, accessToken);
  }

  @Override
  public void addOperatorsToOrgSubUnit(Long organizationId, String subUnitCode, List<String> mappedExternalUserIds, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    orgSubUnitOperatorsService.addOperatorsToOrgSubUnit(organizationId, subUnitCode, mappedExternalUserIds, accessToken);
  }

  @Override
  public void deleteOperatorsFromOrgSubUnit(Long organizationId, String subUnitCode, List<String> mappedExternalUserIds, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    orgSubUnitOperatorsService.deleteOperatorsFromOrgSubUnit(organizationId, subUnitCode, mappedExternalUserIds, accessToken);
  }
}
