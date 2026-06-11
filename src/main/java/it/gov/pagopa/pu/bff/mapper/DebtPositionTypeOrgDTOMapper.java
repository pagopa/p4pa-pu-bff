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
  @Mapping(target = "code", source = "debtPositionTypeOrg.code")
  @Mapping(target = "description", source= "debtPositionTypeOrg.description")
  @Mapping(target = "debtPositionTypeId", source = "debtPositionTypeOrg.debtPositionTypeId")
  @Mapping(target = "flagAnonymousFiscalCode", source = "debtPositionTypeOrg.flagAnonymousFiscalCode")
  @Mapping(target = "flagMandatoryDueDate", source = "debtPositionTypeOrg.flagMandatoryDueDate")
  @Mapping(target = "flagNotifyIo", source = "debtPositionTypeOrg.flagNotifyIo")
  @Mapping(target = "ioTemplateSubject", source = "debtPositionTypeOrg.ioTemplateSubject")
  @Mapping(target = "ioTemplateMessage", source = "debtPositionTypeOrg.ioTemplateMessage")
  @Mapping(target = "debtPositionTypeDescription", source = "debtPositionType.description")
  @Mapping(target = "debtPositionTypeCode", source = "debtPositionType.code")
  @Mapping(target = "debtPositionTypeTaxonomyCode", source = "debtPositionType.taxonomyCode")
  @Mapping(target = "notifyOutcomePushOrgSilServiceApplicationName", source = "notifyOutcomePushOrgSilServiceApplicationName")
  @Mapping(target = "amountActualizationOrgSilServiceApplicationName", source = "amountActualizationOrgSilServiceApplicationName")
  @Mapping(target = "spontaneousFormCode", source = "spontaneousFormCode")
  @Mapping(target = "debtPositionTypeOrgBalanceCosts", source = "debtPositionTypeOrgBalanceCosts")
  DebtPositionTypeOrgDTO map(
    DebtPositionTypeOrg debtPositionTypeOrg,
    DebtPositionType debtPositionType,
    String notifyOutcomePushOrgSilServiceApplicationName,
    String amountActualizationOrgSilServiceApplicationName,
    String spontaneousFormCode,
    List<DebtPositionTypeOrgBalanceCostDTO> debtPositionTypeOrgBalanceCosts
  );
}

