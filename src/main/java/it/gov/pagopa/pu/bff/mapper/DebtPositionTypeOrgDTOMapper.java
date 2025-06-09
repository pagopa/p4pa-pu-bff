package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DebtPositionTypeOrgDTOMapper {

  @Mapping(target = "debtPositionTypeDescription", source = "debtPositionTypeDescription")
  @Mapping(target = "debtPositionTypeCode", source = "debtPositionTypeCode")
  @Mapping(target = "notifyOutcomePushOrgSilServiceApplicationName", source = "notifyOutcomePushOrgSilServiceApplicationName")
  @Mapping(target = "amountActualizationOrgSilServiceApplicationName", source = "amountActualizationOrgSilServiceApplicationName")
  DebtPositionTypeOrgDTO map(DebtPositionTypeOrg debtPositionTypeOrg, String debtPositionTypeDescription, String debtPositionTypeCode,
                             String notifyOutcomePushOrgSilServiceApplicationName, String amountActualizationOrgSilServiceApplicationName);

  default DebtPositionTypeOrgDTO map(DebtPositionTypeOrg debtPositionTypeOrg, DebtPositionType debtPositionType,
                                     String notifyOutcomePushOrgSilServiceApplicationName, String amountActualizationOrgSilServiceApplicationName) {
    String description = null;
    String code = null;

    if (debtPositionType != null) {
      description = debtPositionType.getDescription();
      code = debtPositionType.getCode();
    }

    return map(debtPositionTypeOrg, description, code, notifyOutcomePushOrgSilServiceApplicationName, amountActualizationOrgSilServiceApplicationName);
  }
}

