package it.gov.pagopa.pu.bff.service.organization;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.OrganizationDTOMapper;
import it.gov.pagopa.pu.bff.mapper.OrganizationWithDebtPositionTypeOrgCountMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class OrganizationRetrieverServiceImpl implements OrganizationRetrieverService {

  private final AuthorizationService authorizationService;

  private final OrganizationService organizationService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  private final OrganizationDTOMapper organizationDTOMapper;
  private final OrganizationWithDebtPositionTypeOrgCountMapper organizationWithDebtPositionTypeOrgCountMapper;

  public OrganizationRetrieverServiceImpl(
    AuthorizationService authorizationService, OrganizationService organizationService,
    DebtPositionTypeOrgService debtPositionTypeOrgService, OrganizationDTOMapper organizationDTOMapper,
    OrganizationWithDebtPositionTypeOrgCountMapper organizationWithDebtPositionTypeOrgCountMapper) {
    this.authorizationService = authorizationService;
    this.organizationService = organizationService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.organizationDTOMapper = organizationDTOMapper;
    this.organizationWithDebtPositionTypeOrgCountMapper = organizationWithDebtPositionTypeOrgCountMapper;
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
      throw new ResourceNotFoundException("Organization having organizationId "+ organizationId +" not found");
    }
  }
}
