package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorRole;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.exception.InvalidOperatorRoleException;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface OperatorDetailMapper {

  @Mapping(target = "pagedDebtPositionTypeOrg", source = "pagedModelDebtPositionTypeOrg")
  @Mapping(target = "operatorId", source = "organizationOperator.operatorId")
  @Mapping(target = "operatorFiscalCode", source = "organizationOperator.fiscalCode")
  @Mapping(target = "operatorRole", source = "organizationOperator.roles", qualifiedByName = "determineOperatorRole")
  @Mapping(target= "operatorName", source = "organizationOperator.firstName")
  @Mapping(target= "operatorLastName", source = "organizationOperator.lastName")
  @Mapping(target= "operatorEmail", source= "organizationOperator.email")
  OperatorsDetail map(PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg, OperatorDTO organizationOperator, @Context Map<Long, DebtPositionType> debtPositionTypes);

  default PagedDebtPositionTypeOrgDTO mapToPagedDebtPositionTypeOrgDTO(PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg, @Context Map<Long, DebtPositionType> debtPositionTypes) {
    return Mappers.getMapper(PagedDebtPositionTypeOrgDTOMapper.class).map(pagedModelDebtPositionTypeOrg, debtPositionTypes);
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
      throw new InvalidOperatorRoleException("INVALID_OPERATOR_ROLE", "INVALID_OPERATOR_ROLE: " + operatorRoleValue);
    }
  }
}
