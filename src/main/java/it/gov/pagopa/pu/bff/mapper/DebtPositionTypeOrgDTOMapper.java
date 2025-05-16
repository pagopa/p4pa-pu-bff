package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DebtPositionTypeOrgDTOMapper {
    DebtPositionTypeOrgDTO map(DebtPositionTypeOrg debtPositionTypeOrg);
}
