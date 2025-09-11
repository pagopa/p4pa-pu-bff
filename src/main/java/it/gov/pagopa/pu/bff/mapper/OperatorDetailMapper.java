package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedOperatorsDetails;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OperatorDetailMapper {

  @Mapping(target = "pagedOperatorsDetails", source = "pagedModelDebtPositionTypeOrg")
  @Mapping(target = "operatorId", source = "organizationOperator.operatorId")
  @Mapping(target = "operatorFiscalCode", source = "organizationOperator.fiscalCode")
  @Mapping(target = "operatorRole", source = "organizationOperator.roles")
  @Mapping(target= "operatorName", source = "organizationOperator.firstName")
  @Mapping(target= "operatorLastName", source = "organizationOperator.lastName")
  OperatorsDetail map(PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg, OperatorDTO organizationOperator);

  default PagedOperatorsDetails mapToPagedOperatorsDetails(PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg) {
    return Mappers.getMapper(PagedOperatorsDetailsMapper.class).map(pagedModelDebtPositionTypeOrg);
  }
}
