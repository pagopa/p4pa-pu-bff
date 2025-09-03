package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper {

  @Mapping(target = "content", source = "source.embedded.organizations")
  @Mapping(target = "totalPages", source = "source.page.totalPages")
  @Mapping(target = "size", source = "source.page.size")
  @Mapping(target = "number", source = "source.page.number")
  @Mapping(target = "totalElements", source = "source.page.totalElements")
  PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount map(PagedModelOrganization source, @Context List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId,@Context List<OperatorsPage> allOperatorsPages);

  @Mapping(target = "debtPositionTypeOrgCount", expression = "java(dptoCount(organization, dptoCountsByOrgId))")
  @Mapping(target = "operatorsCount", expression = "java(operatorsCount(organization, allOperatorsPages))")
  OrganizationWithDebtPositionTypeOrgAndOperatorsCount mapToOrganizationWithDebtPositionTypeOrgAndOperatorsCount(Organization organization, @Context List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId, @Context List<OperatorsPage> allOperatorsPages);

  default Integer dptoCount(Organization organization, List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId){
    Map<Long, Integer> dptoCountByOrgId = dptoCountsByOrgId.stream()
      .filter(dpto -> dpto.getOrganizationId() != null &&
        dpto.getActiveOrganizations() != null)
      .collect(Collectors.toMap(
        DebtPositionTypeOrgCountByOrganizationId::getOrganizationId,
        DebtPositionTypeOrgCountByOrganizationId::getActiveOrganizations));
    return dptoCountByOrgId.getOrDefault(organization.getOrganizationId(),0);
  }

  default Integer operatorsCount(Organization organization, List<OperatorsPage> allOperators) {
    return allOperators.stream()
      .filter(operatorsPage -> operatorsPage != null && !operatorsPage.getContent().isEmpty())
      .flatMap(operatorsPage ->
        operatorsPage.getContent().stream()
          .filter(operatorDTO -> operatorDTO.getOrganizationIpaCode().equals(organization.getIpaCode())))
      .toList().size();
  }

}
