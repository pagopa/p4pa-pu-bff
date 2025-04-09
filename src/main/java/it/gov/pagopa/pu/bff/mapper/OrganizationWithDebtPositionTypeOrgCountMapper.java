package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PageMetadata;
import java.util.List;
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
        // TODO: check what to do if no DPTO matches with orgId
        DebtPositionTypeOrgCountByOrganizationId dptoCountByOrgId = dptoCountsByOrgId.stream()
          .filter(dpto -> o.getOrganizationId().equals(dpto.getOrganizationId()))
          .findFirst().orElse(null);
        return mapToOrganizationWithDebtPositionTypeOrgCount(o,
          dptoCountByOrgId);
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
    Organization organization, DebtPositionTypeOrgCountByOrganizationId dpto) {
    return OrganizationWithDebtPositionTypeOrgCount.builder()
      .organizationId(organization.getOrganizationId())
      .ipaCode(organization.getIpaCode())
      .organizationName(organization.getOrgName())
      // TODO: check what to do in case dpto is null
      .debtPositionTypeOrgCount(
        dpto != null ? dpto.getActiveOrganizations() : Integer.valueOf(0))
      .build();
  }
}
