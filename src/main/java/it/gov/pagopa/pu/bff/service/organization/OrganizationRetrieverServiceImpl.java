package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationDetailMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationWithDebtPositionTypeOrgCountMapper;
import it.gov.pagopa.pu.bff.mapper.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import it.gov.pagopa.pu.auth.dto.generated.UserOrganizationRoles;

import java.util.*;
import java.util.stream.Collectors;

import static it.gov.pagopa.pu.bff.util.Utilities.checkImmutableField;

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
  private final OrganizationDetailMapper organizationDetailMapper;

  public OrganizationRetrieverServiceImpl(
    AuthorizationService authorizationService,
    OrganizationService organizationService,
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    OrganizationDTOMapper organizationDTOMapper,
    OrganizationWithDebtPositionTypeOrgCountMapper organizationWithDebtPositionTypeOrgCountMapper,
    AuthzService authzService,
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper,
    OrganizationDetailMapper organizationDetailMapper) {
    this.authorizationService = authorizationService;
    this.organizationService = organizationService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.organizationDTOMapper = organizationDTOMapper;
    this.organizationWithDebtPositionTypeOrgCountMapper = organizationWithDebtPositionTypeOrgCountMapper;
    this.authzService = authzService;
    this.pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper = pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper;
    this.organizationDetailMapper = organizationDetailMapper;
  }

  @Override
  public List<OrganizationDTO> getOrganizations(UserInfo userInfo, String accessToken) {
    return userInfo.getOrganizations().stream()
      .map(orgRoles -> Optional.ofNullable(
          organizationService.getOrganizationByIpaCode(orgRoles.getOrganizationIpaCode(), accessToken))
        .filter(organization -> Objects.equals(organization.getBrokerId(), userInfo.getBrokerId()))
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
  public PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount getOrganizationsByBrokerIdAndFilters(UserInfo userInfo, String orgName, String ipaCode, Pageable pageable, String accessToken) {
    authorizationService.validateBrokerAdminRole(userInfo);

    Set<Long> allowedOrganizationIds = userInfo.getOrganizations().stream()
      .map(UserOrganizationRoles::getOrganizationId)
      .collect(Collectors.toSet());

    PagedModelOrganization pagedModelOrganization = organizationService.getOrganizationsByBrokerIdAndFilters(userInfo.getBrokerId(), orgName, ipaCode, allowedOrganizationIds, pageable, accessToken);

    if (pagedModelOrganization == null
      || pagedModelOrganization.getEmbedded() == null
      || CollectionUtils.isEmpty(pagedModelOrganization.getEmbedded().getOrganizations())) {
      log.info("No results for getOrganizationsByBrokerIdAndFilters");
      return pagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper.map(pagedModelOrganization, Collections.emptyMap(), Collections.emptyMap());
    }

    List<Organization> organizationList = pagedModelOrganization.getEmbedded().getOrganizations();

    List<Long> organizationIds = organizationList.stream()
      .map(Organization::getOrganizationId)
      .toList();

    Map<Long, Integer> dptoCountsByOrgId = getDptoCountsByOrgIdMap(accessToken, organizationIds);
    Map<Long, OperatorsPage> allOperatorsPages = getOperatorsPageMap(accessToken, organizationList);

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

  private Map<Long, OperatorsPage> getOperatorsPageMap(String accessToken, List<Organization> orgList) {
    Map<Long, OperatorsPage> allOperatorsPages = new HashMap<>();

    orgList
      .forEach(org -> {
        OperatorsPage organizationOperators = authzService.getOrganizationOperators(
          org.getIpaCode(),
          null,
          null,
          null,
          0,
          1,
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
  public OrganizationDetail getOrganizationDetail(Long organizationId, UserInfo loggedUser, String accessToken) {
    authorizationService.validateOrganizationOrBrokerAdmin(organizationId, loggedUser, accessToken);

    Organization organization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if (organization == null) {
      throw new ResourceNotFoundException("Organization having organizationId " + organizationId + " not found");
    }

    OrganizationDetailDTO orgDetail = organizationService.getOrganizationDetail(organizationId, accessToken);

    Map<Long, Integer> dptoCountsByOrgId = getDptoCountsByOrgIdMap(accessToken, List.of(organizationId));
    Integer debtPositionTypeOrgCount = dptoCountsByOrgId.getOrDefault(organizationId, 0);

    Map<Long, OperatorsPage> operatorsPageMap = getOperatorsPageMap(accessToken, List.of(organization));
    OperatorsPage operatorsPage = operatorsPageMap.get(organizationId);

    OrganizationDetail organizationDetail = organizationDetailMapper.mapToBffDTO(orgDetail);
    organizationDetail.setDebtPositionTypeOrgCount(debtPositionTypeOrgCount);
    organizationDetail.setOperatorsCount(operatorsPage != null ? operatorsPage.getTotalElements() : 0);

    return organizationDetail;
  }

  @Override
  public void updateOrganization(Long organizationId, OrganizationDetailDTO organizationDetailDTO, UserInfo loggedUser, String accessToken) {
    authorizationService.validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    validateOrganization(organizationId, organizationDetailDTO, accessToken);
    organizationService.updateOrganization(organizationDetailDTO,accessToken);
  }

  private void validateOrganization(Long organizationId, OrganizationDetailDTO organizationDetailDTO, String accessToken) {
    if(!organizationId.equals(organizationDetailDTO.getOrganizationId())){
      throw new InvalidOrganizationException("The Organization's id " + organizationDetailDTO.getOrganizationId() +
              " does not match the given organizationId "+ organizationId);
    }
    Organization existingOrganization = organizationService.getOrganizationByOrganizationId(organizationId, accessToken);
    if(existingOrganization==null){
      throw new ResourceNotFoundException("Organization having id "+ organizationId +" not found");
    }
    checkReadOnlyFields(existingOrganization, organizationDetailDTO);
  }

  private void checkReadOnlyFields(Organization existingOrganization, OrganizationDetailDTO organization) {
    List<String> modifiedFields = new ArrayList<>();
    checkImmutableField("brokerId", existingOrganization.getBrokerId(), organization.getBrokerId(), modifiedFields);
    checkImmutableField("externalOrganizationId", existingOrganization.getExternalOrganizationId(), organization.getExternalOrganizationId(), modifiedFields);
    checkImmutableField("ipaCode", existingOrganization.getIpaCode(), organization.getIpaCode(), modifiedFields);
    checkImmutableField("orgFiscalCode", existingOrganization.getOrgFiscalCode(), organization.getOrgFiscalCode(), modifiedFields);
    checkImmutableField("orgName", existingOrganization.getOrgName(), organization.getOrgName(), modifiedFields);
    checkImmutableField("orgTypeCode", existingOrganization.getOrgTypeCode(), organization.getOrgTypeCode(), modifiedFields);
    if(!CollectionUtils.isEmpty(modifiedFields)){
      throw new ValidationException("The following Organization fields are readOnly. "+modifiedFields);
    }
  }
}
