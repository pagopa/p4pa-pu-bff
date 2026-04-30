package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentsRegistryDTOMapper {

  @Mapping(target = "debtPositionTypeOrgDescription", source = "debtPositionTypeOrgDescription")
  AssessmentsRegistryDTO map(AssessmentsRegistry assessmentsRegistry, String debtPositionTypeOrgDescription);
}
