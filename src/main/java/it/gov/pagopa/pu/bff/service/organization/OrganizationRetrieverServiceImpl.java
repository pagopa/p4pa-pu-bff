package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationWithDebtPositionTypeOrgCountMapper;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizationRetrieverServiceImpl implements OrganizationRetrieverService {

  private final AuthorizationService authorizationService;
  private final OrganizationService organizationService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final OrganizationDTOMapper organizationDTOMapper;
  private final OrganizationWithDebtPositionTypeOrgCountMapper organizationWithDebtPositionTypeOrgCountMapper;
  private final AuthzService authzService;
  private final PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper;

  public OrganizationRetrieverServiceImpl(
    AuthorizationService authorizationService, OrganizationService organizationService,
    DebtPositionTypeOrgService debtPositionTypeOrgService, OrganizationDTOMapper organizationDTOMapper,
    OrganizationWithDebtPositionTypeOrgCountMapper organizationWithDebtPositionTypeOrgCountMapper, AuthzService authzService, PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper) {
    this.authorizationService = authorizationService;
    this.organizationService = organizationService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.organizationDTOMapper = organizationDTOMapper;
    this.organizationWithDebtPositionTypeOrgCountMapper = organizationWithDebtPositionTypeOrgCountMapper;
    this.authzService = authzService;
    this.pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper = pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper;
  }

  @Override
  public List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken) {
    return userInfo.getOrganizations().stream()
      .map(orgRoles -> Optional.ofNullable(
          organizationService.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode(), accessToken))
        .map(organization -> organizationDTOMapper.mapToOrganizationDTO(organization, orgRoles.getRoles()))
        .orElse(null)
      ).filter(Objects::nonNull).toList();
  }

  @Override
  public PagedOrganizationWithDebtPositionTypeOrgCount getOrganizationsWithDebtPositionTypeOrgCount(
    Long organizationId, String organizationName, Pageable pageable,
    UserInfo loggedUser,
    String accessToken) {
    authorizationService.validateBrokerAdminRole(loggedUser);

    PagedModelOrganization pagedOrganizations = organizationService.getOrganizationByBrokerIdAndOrgName(
      loggedUser.getBrokerId(), organizationName, pageable,
      accessToken);

    if (pagedOrganizations == null ||
      pagedOrganizations.getEmbedded() == null ||
      CollectionUtils.isEmpty(pagedOrganizations.getEmbedded().getOrganizations())) {
      log.info("No results for getOrganizationsWithDebtPositionTypeOrgCount");
      return PagedOrganizationWithDebtPositionTypeOrgCount.builder()
        .content(Collections.emptyList())
        .size(0L)
        .totalPages(0L)
        .totalElements(0L)
        .number(0L)
        .build();
    }

    List<Organization> organizations = pagedOrganizations.getEmbedded().getOrganizations();

    List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId = getDebtPositionTypeOrgCountByOrganizationId(
      organizations.stream().map(Organization::getOrganizationId).toList(),
      accessToken
    );

    return organizationWithDebtPositionTypeOrgCountMapper.mapToPagedOrganizationWithDebtPositionTypeOrgCount(
      organizations, dptoCountsByOrgId, pagedOrganizations.getPage());
  }

  @Override
  public PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount getOrganizationsByBrokerId(UserInfo userInfo, Pageable pageable, String accessToken) {
    authorizationService.validateBrokerAdminRole(userInfo);

    PagedModelOrganization pagedModelOrganization = organizationService.getOrganizationsByBrokerId(userInfo.getBrokerId(), pageable, accessToken);

    if (pagedModelOrganization == null || pagedModelOrganization.getEmbedded() == null || pagedModelOrganization.getEmbedded().getOrganizations() == null || pagedModelOrganization.getEmbedded().getOrganizations().isEmpty()) {
      log.info("No results for getOrganizationsByBrokerId");
      return pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper.map(pagedModelOrganization, null, null);
    }

    List<Organization> orgList = pagedModelOrganization.getEmbedded().getOrganizations();

    List<Long> organizationIds = orgList.stream()
      .map(Organization::getOrganizationId)
      .toList();

    Map<Long, Integer> dptoCountsByOrgId = getDptoCountsByOrgIdMap(accessToken, organizationIds);

    Map<Long, OperatorsPage> allOperatorsPages = getOperatorsPageMap(pageable, accessToken, orgList);

    return pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper.map(pagedModelOrganization, dptoCountsByOrgId, allOperatorsPages);
  }

  private Map<Long, Integer> getDptoCountsByOrgIdMap(String accessToken, List<Long> organizationIds) {
    return getDebtPositionTypeOrgCountByOrganizationId(
      organizationIds,
      accessToken
    )
      .stream()
      .filter(dpto -> dpto.getOrganizationId() != null && dpto.getActiveOrganizations() != null)
      .collect(Collectors.toMap(
        DebtPositionTypeOrgCountByOrganizationId::getOrganizationId,
        DebtPositionTypeOrgCountByOrganizationId::getActiveOrganizations));
  }

  private Map<Long, OperatorsPage> getOperatorsPageMap(Pageable pageable, String accessToken, List<Organization> orgList) {
    Map<Long, OperatorsPage> allOperatorsPages = new HashMap<>();

    orgList
      .forEach(org -> {
        OperatorsPage organizationOperators = authzService.getOrganizationOperators(
          org.getIpaCode(),
          null,
          null,
          null,
          pageable.getPageNumber(),
          pageable.getPageSize(),
          accessToken
        );
        allOperatorsPages.put(org.getOrganizationId(),organizationOperators);
      });
    return allOperatorsPages;
  }

  private List<DebtPositionTypeOrgCountByOrganizationId> getDebtPositionTypeOrgCountByOrganizationId(List<Long> organizationIds, String accessToken) {
    CollectionModelDebtPositionTypeOrgCountByOrganizationId collection = debtPositionTypeOrgService.getDebtPositionTypeOrgCountByOrganizationId(organizationIds, accessToken);

    if (collection == null || collection.getEmbedded() == null) {
      return Collections.emptyList();
    }
    return collection.getEmbedded().getDebtPositionTypeOrgCountByOrganizationIds();
  }

  public String getOrgFiscalCode(Long organizationId, UserInfo loggedUser, String accessToken) {
    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if(organization!=null && organization.getBrokerId()!=null && organization.getBrokerId().equals(loggedUser.getBrokerId())){
      return organization.getOrgFiscalCode();
    }else{
      throw new ResourceNotFoundException("Organization having organizationId "+ organizationId +" and brokerId "+loggedUser.getBrokerId()+" not found");
    }
  }

  @Override
  public OrganizationDetailDTO getOrganizationDetail(Long organizationId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateOrganizationOrBrokerAdmin(organizationId, loggedUser, accessToken);

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if (organization == null) {
      throw new ResourceNotFoundException("Organization having organizationId " + organizationId + " not found");
    }

    return organizationService.getOrganizationDetail(organizationId, accessToken);
  }
}
