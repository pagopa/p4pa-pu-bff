package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface PagedOrganizationOperatorMapper {

    @Mapping(target = "content", expression = "java(mapContentWithDebtPositionTypeOrgCount(source.getContent(), operatorDptoCount, organization))")
    @Mapping(target = "totalPages", source = "source.totalPages")
    @Mapping(target = "size", source = "source.pageSize")
    @Mapping(target = "number", source = "source.pageNo")
    @Mapping(target = "totalElements", source = "source.totalElements")
    PagedOrganizationOperator mapToPagedOrganizationOperator(OperatorsPage source, @Context Map<String, Long> operatorDptoCount, @Context Organization organization);

    List<OrganizationOperator> mapContentWithDebtPositionTypeOrgCount(List<OperatorDTO> content, @Context Map<String, Long> operatorDptoCount, @Context Organization organization);

    @Mapping(target = "debtPositionTypeOrgCount",expression = "java(operatorDptoCount!=null && operatorDptoCount.containsKey(operator.getMappedExternalUserId())?operatorDptoCount.get(operator.getMappedExternalUserId()):0L)")
    @Mapping(target = "orgName", expression = "java(organization != null ? organization.getOrgName() : null)")
    OrganizationOperator mapWithDebtPositionTypeOrgCount(OperatorDTO operator, @Context Map<String, Long> operatorDptoCount, @Context Organization organization);
}


