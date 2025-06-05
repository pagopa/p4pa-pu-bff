package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DebtPositionTypeOrgDTOMapper {
  @Mapping(target = "debtPositionTypeDescription", source = "debtPositionTypeDescription")
  @Mapping(target = "debtPositionTypeCode", source = "debtPositionTypeCode")
  DebtPositionTypeOrgDTO map(DebtPositionTypeOrg debtPositionTypeOrg, String debtPositionTypeDescription, String debtPositionTypeCode);
}
