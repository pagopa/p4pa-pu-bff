package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PageMetadata;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrganizationWithDebtPositionTypeOrgCountMapper {

  public PagedOrganizationWithDebtPositionTypeOrgCount mapToPagedOrganizationWithDebtPositionTypeOrgCount(
    List<Organization> organizations,
    List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId,
    PageMetadata pageMetadata) {
    List<OrganizationWithDebtPositionTypeOrgCount> content = organizations.stream()
      .filter(o -> o.getOrganizationId() != null)
      .map(o -> {
        Map<Long, Integer> dptoCountByOrgId = dptoCountsByOrgId.stream()
          .filter(dpto -> dpto.getOrganizationId() != null &&
            dpto.getActiveOrganizations() != null)
          .collect(Collectors.toMap(
            DebtPositionTypeOrgCountByOrganizationId::getOrganizationId,
            DebtPositionTypeOrgCountByOrganizationId::getActiveOrganizations));
        return mapToOrganizationWithDebtPositionTypeOrgCount(o, dptoCountByOrgId);
      })
      .toList();

    return PagedOrganizationWithDebtPositionTypeOrgCount.builder()
      .content(content)
      .number(pageMetadata.getNumber())
      .size(pageMetadata.getSize())
      .totalPages(pageMetadata.getTotalPages())
      .totalElements(pageMetadata.getTotalElements())
      .build();
  }

  private OrganizationWithDebtPositionTypeOrgCount mapToOrganizationWithDebtPositionTypeOrgCount(
    Organization organization, Map<Long, Integer> dptoCountByOrgId) {
    return OrganizationWithDebtPositionTypeOrgCount.builder()
      .organizationId(organization.getOrganizationId())
      .ipaCode(organization.getIpaCode())
      .organizationName(organization.getOrgName())
      .debtPositionTypeOrgCount(dptoCountByOrgId.getOrDefault(organization.getOrganizationId(), 0))
      .build();
  }
}
