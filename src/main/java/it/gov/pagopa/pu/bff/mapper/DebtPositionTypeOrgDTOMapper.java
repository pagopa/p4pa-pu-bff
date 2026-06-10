package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DebtPositionTypeOrgDTOMapper {

  @Mapping(target = "debtPositionTypeDescription", source = "debtPositionTypeDescription")
  @Mapping(target = "debtPositionTypeCode", source = "debtPositionTypeCode")
  @Mapping(target = "debtPositionTypeTaxonomyCode", source = "debtPositionTypeTaxonomyCode")
  @Mapping(target = "notifyOutcomePushOrgSilServiceApplicationName", source = "notifyOutcomePushOrgSilServiceApplicationName")
  @Mapping(target = "amountActualizationOrgSilServiceApplicationName", source = "amountActualizationOrgSilServiceApplicationName")
  @Mapping(target = "spontaneousFormCode", source = "spontaneousFormCode")
  @Mapping(target = "debtPositionTypeOrgBalanceCosts", source = "debtPositionTypeOrgBalanceCosts")
  DebtPositionTypeOrgDTO map(DebtPositionTypeOrg debtPositionTypeOrg, String debtPositionTypeDescription, String debtPositionTypeCode, String debtPositionTypeTaxonomyCode,
                             String notifyOutcomePushOrgSilServiceApplicationName, String amountActualizationOrgSilServiceApplicationName, String spontaneousFormCode,
                             List<DebtPositionTypeOrgBalanceCostDTO> debtPositionTypeOrgBalanceCosts);

  default DebtPositionTypeOrgDTO map(DebtPositionTypeOrg debtPositionTypeOrg, DebtPositionType debtPositionType,
                                     String notifyOutcomePushOrgSilServiceApplicationName, String amountActualizationOrgSilServiceApplicationName,
                                     String spontaneousFormCode, List<DebtPositionTypeOrgBalanceCostDTO> debtPositionTypeOrgBalanceCosts) {
    String description = null;
    String code = null;
    String taxonomyCode = null;

    if (debtPositionType != null) {
      description = debtPositionType.getDescription();
      code = debtPositionType.getCode();
      taxonomyCode = debtPositionType.getTaxonomyCode();
    }

    return map(debtPositionTypeOrg, description, code, taxonomyCode, notifyOutcomePushOrgSilServiceApplicationName, amountActualizationOrgSilServiceApplicationName, spontaneousFormCode, debtPositionTypeOrgBalanceCosts);
  }
}

