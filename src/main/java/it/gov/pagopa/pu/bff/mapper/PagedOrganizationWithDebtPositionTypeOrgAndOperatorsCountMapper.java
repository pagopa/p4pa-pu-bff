package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper {

  @Mapping(target = "content", source = "source.embedded.organizations")
  @Mapping(target = "totalPages", source = "source.page.totalPages")
  @Mapping(target = "size", source = "source.page.size")
  @Mapping(target = "number", source = "source.page.number")
  @Mapping(target = "totalElements", source = "source.page.totalElements")
  PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount map(PagedModelOrganization source, @Context Map<Long, Integer> dptoCountsByOrgId,@Context Map<Long, OperatorsPage> allOperatorsPages);

  @Mapping(target = "debtPositionTypeOrgCount", expression = "java(dptoCount(organization, dptoCountsByOrgId))")
  @Mapping(target = "operatorsCount", expression = "java(operatorsCount(organization, allOperatorsPages))")
  OrganizationWithDebtPositionTypeOrgAndOperatorsCount mapToOrganizationWithDebtPositionTypeOrgAndOperatorsCount(Organization organization, @Context  Map<Long, Integer> dptoCountsByOrgId, @Context Map<Long, OperatorsPage> allOperatorsPages);

  default Integer dptoCount(Organization organization,  Map<Long, Integer> dptoCountsByOrgId){
    return dptoCountsByOrgId.getOrDefault(organization.getOrganizationId(),0);
  }

  default Integer operatorsCount(Organization organization, Map<Long, OperatorsPage> allOperators) {
    OperatorsPage operatorsPage = allOperators.get(organization.getOrganizationId());

    if (operatorsPage != null && !operatorsPage.getContent().isEmpty()) {
      return operatorsPage.getTotalElements();
    }

    return 0;
  }

}
