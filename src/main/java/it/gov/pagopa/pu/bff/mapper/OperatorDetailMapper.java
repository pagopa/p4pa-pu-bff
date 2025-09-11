package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorRole;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOperatorsDetails;
import it.gov.pagopa.pu.bff.exception.InvalidOperatorRoleException;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperatorDetailMapper {

  @Mapping(target = "pagedOperatorsDetails", source = "pagedModelDebtPositionTypeOrg")
  @Mapping(target = "operatorId", source = "organizationOperator.operatorId")
  @Mapping(target = "operatorFiscalCode", source = "organizationOperator.fiscalCode")
  @Mapping(target = "operatorRole", source = "organizationOperator.roles", qualifiedByName = "determineOperatorRole")
  @Mapping(target= "operatorName", source = "organizationOperator.firstName")
  @Mapping(target= "operatorLastName", source = "organizationOperator.lastName")
  OperatorsDetail map(PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg, OperatorDTO organizationOperator);

  default PagedOperatorsDetails mapToPagedOperatorsDetails(PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg) {
    return Mappers.getMapper(PagedOperatorsDetailsMapper.class).map(pagedModelDebtPositionTypeOrg);
  }

  @Named("determineOperatorRole")
  static OperatorRole determineOperatorRole(List<String> roles) {
    if (roles == null || roles.isEmpty()) {
      return null;
    }

    String operatorRoleValue = roles.stream()
            .filter("ROLE_ADMIN"::equals)
            .findFirst()
            .orElse(roles.getFirst());

    try {
      return OperatorRole.fromValue(operatorRoleValue);
    } catch (IllegalArgumentException e) {
      throw new InvalidOperatorRoleException("INVALID_OPERATOR_ROLE: " + operatorRoleValue);
    }
  }
}
