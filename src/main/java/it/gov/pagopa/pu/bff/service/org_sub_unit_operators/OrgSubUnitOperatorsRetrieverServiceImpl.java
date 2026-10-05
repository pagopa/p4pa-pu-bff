package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitOperatorsService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.OrgSubUnitAvailableOperatorsFilters;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrgSubUnitOperatorsRetrieverServiceImpl implements OrgSubUnitOperatorsRetrieverService {

  private static final int AUTH_OPERATORS_FETCH_SIZE = 2000;

  private final AuthorizationService authorizationService;
  private final OrgSubUnitOperatorsService orgSubUnitOperatorsService;
  private final PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapper;
  private final AuthzService authzService;
  private final OrganizationService organizationService;

  public OrgSubUnitOperatorsRetrieverServiceImpl(
    AuthorizationService authorizationService,
    OrgSubUnitOperatorsService orgSubUnitOperatorsService,
    PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapper,
    AuthzService authzService,
    OrganizationService organizationService) {
    this.authorizationService = authorizationService;
    this.orgSubUnitOperatorsService = orgSubUnitOperatorsService;
    this.pagedOrgSubUnitOperatorsMapper = pagedOrgSubUnitOperatorsMapper;
    this.authzService = authzService;
    this.organizationService = organizationService;
  }

  @Override
  public PagedOrgSubUnitOperators getOrgSubUnitOperators(Long organizationId, String subUnitCode, OrgSubUnitOperatorsFilters filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);

    String organizationIpaCode = organization.getIpaCode();

    final Map<String, OperatorDTO> operatorsMap;
    PagedModelOrgSubUnitOperators pagedModelOrgSubUnitOperators;
    List<OrgSubUnitOperators> orgSubUnitOperators;


    if (hasPiiFilters(filters)) {
      operatorsMap = retrieveOperatorsMap(organizationIpaCode, filters.getMappedExternalUserId(), filters.getFiscalCode(), filters.getFirstName(),filters.getLastName(), accessToken);

      if (operatorsMap.isEmpty()) {
        return buildEmptyPage(pageable);
      }

      pagedModelOrgSubUnitOperators = orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(organizationId, subUnitCode, operatorsMap.keySet(), pageable, accessToken);

      orgSubUnitOperators = extractOperators(pagedModelOrgSubUnitOperators);

    } else {
      if (StringUtils.hasText(filters.getMappedExternalUserId())) {
        pagedModelOrgSubUnitOperators =
          orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(organizationId, subUnitCode, Set.of(filters.getMappedExternalUserId()), pageable, accessToken);

      }  else {
        pagedModelOrgSubUnitOperators = orgSubUnitOperatorsService.findByOrganizationIdAndSubUnitCode(organizationId, subUnitCode, pageable, accessToken);
      }

      orgSubUnitOperators = extractOperators(pagedModelOrgSubUnitOperators);
      operatorsMap = retrieveOperatorsInfo(organizationIpaCode, orgSubUnitOperators, accessToken);
    }

    List<OrgSubUnitOperator> content = orgSubUnitOperators.stream()
      .map(operator -> enrichWithOperatorInfo(operator, operatorsMap))
      .toList();

    return pagedOrgSubUnitOperatorsMapper.map(content, pagedModelOrgSubUnitOperators);
  }

  private boolean hasPiiFilters(OrgSubUnitOperatorsFilters filters) {
    return StringUtils.hasText(filters.getFiscalCode())
      || StringUtils.hasText(filters.getFirstName())
      || StringUtils.hasText(filters.getLastName());
  }

  private Map<String, OperatorDTO> retrieveOperatorsMap(String organizationIpaCode, String mappedExternalUserId, String fiscalCode, String firstName, String lastName, String accessToken) {
    if (StringUtils.hasText(mappedExternalUserId)) {
      return retrieveSingleOperator(organizationIpaCode, mappedExternalUserId, fiscalCode, firstName, lastName, accessToken);
    }
    return retrieveOperators(organizationIpaCode, fiscalCode, firstName, lastName, accessToken);
  }

  private Map<String, OperatorDTO> retrieveSingleOperator(String organizationIpaCode, String mappedExternalUserId, String fiscalCode, String firstName, String lastName, String accessToken) {
    OperatorDTO operator = authzService.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken);

    if (operator == null || !matchesFilters(operator, fiscalCode, firstName, lastName)) {
      return Collections.emptyMap();
    }

    return Map.of(operator.getMappedExternalUserId(), operator);
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

  private Map<String, OperatorDTO> retrieveOperators(String organizationIpaCode, String fiscalCode, String firstName, String lastName, String accessToken) {
    OperatorsPage operatorsPage = authzService.getOrganizationOperators(organizationIpaCode, fiscalCode, firstName, lastName, 0, AUTH_OPERATORS_FETCH_SIZE, accessToken);

    return operatorsPage.getContent().stream()
      .collect(Collectors.toMap(OperatorDTO::getMappedExternalUserId, Function.identity()));
  }

  private PagedOrgSubUnitOperators buildEmptyPage(Pageable pageable) {
    PagedOrgSubUnitOperators result = new PagedOrgSubUnitOperators();

    result.setContent(Collections.emptyList());
    result.setNumber((long) pageable.getPageNumber());
    result.setSize((long) pageable.getPageSize());
    result.setTotalElements(0L);
    result.setTotalPages(0L);

    return result;
  }

  private List<OrgSubUnitOperators> extractOperators(PagedModelOrgSubUnitOperators paged) {
    return Optional.ofNullable(paged.getEmbedded())
      .map(PagedModelOrgSubUnitOperatorsEmbedded::getOrgSubUnitOperatorses)
      .orElse(Collections.emptyList());
  }

  private Map<String, OperatorDTO> retrieveOperatorsInfo(String organizationIpaCode, List<OrgSubUnitOperators> operators, String accessToken) {
    return operators.stream()
      .map(OrgSubUnitOperators::getOperatorExternalUserId)
      .distinct()
      .map(mappedExternalUserId -> authzService.getOrganizationOperator(organizationIpaCode, mappedExternalUserId, accessToken))
      .filter(Objects::nonNull)
      .collect(Collectors.toMap(OperatorDTO::getMappedExternalUserId, Function.identity()));
  }

  private OrgSubUnitOperator enrichWithOperatorInfo(OrgSubUnitOperators sourceOperator, Map<String, OperatorDTO> operatorsMap) {
    OperatorDTO operatorDTO = operatorsMap.get(sourceOperator.getOperatorExternalUserId());
    return pagedOrgSubUnitOperatorsMapper.toOrgSubUnitOperator(sourceOperator, operatorDTO);
  }

  @Override
  public PagedOrgSubUnitOperators getOrgSubUnitAvailableOperators(Long organizationId, String subUnitCode, OrgSubUnitAvailableOperatorsFilters filters, Pageable pageable, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    String organizationIpaCode = organization.getIpaCode();

    OperatorsPage operatorsPage = authzService.getOrganizationOperators(
      organizationIpaCode, filters.getFiscalCode(), filters.getFirstName(), filters.getLastName(), 0, AUTH_OPERATORS_FETCH_SIZE, accessToken
    );

    List<OperatorDTO> allCandidateOperators = operatorsPage.getContent();
    if (allCandidateOperators.isEmpty()) {
      return buildEmptyPage(pageable);
    }

    Set<String> alreadyAssociatedIds = getAlreadyAssociatedOperatorIds(organizationId, subUnitCode, allCandidateOperators, accessToken);

    List<OperatorDTO> availableOperators = allCandidateOperators.stream()
      .filter(op -> !alreadyAssociatedIds.contains(op.getMappedExternalUserId()))
      .toList();

    Page<OrgSubUnitOperator> paginatedPage = paginateAndMapToDto(availableOperators, pageable);

    return pagedOrgSubUnitOperatorsMapper.map(paginatedPage);
  }

  private Set<String> getAlreadyAssociatedOperatorIds(Long organizationId, String subUnitCode, List<OperatorDTO> candidates, String accessToken) {
    Set<String> candidateIds = candidates.stream()
      .map(OperatorDTO::getMappedExternalUserId)
      .collect(Collectors.toSet());

    PagedModelOrgSubUnitOperators pagedAssociatedOperators = orgSubUnitOperatorsService
      .findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(
        organizationId,
        subUnitCode,
        candidateIds,
        PageRequest.of(0, candidateIds.size()),
        accessToken
      );

    return extractOperators(pagedAssociatedOperators).stream()
      .map(OrgSubUnitOperators::getOperatorExternalUserId)
      .collect(Collectors.toSet());
  }

  private Page<OrgSubUnitOperator> paginateAndMapToDto(List<OperatorDTO> availableOperators, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), availableOperators.size());

    List<OrgSubUnitOperator> content = availableOperators.subList(start, end).stream()
      .map(pagedOrgSubUnitOperatorsMapper::toOrgSubUnitOperator)
      .toList();

    return new PageImpl<>(content, pageable, availableOperators.size());
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
