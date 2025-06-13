package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssessmentsRegistryDTOMapper {

  AssessmentsRegistryDTO map(AssessmentsRegistry assessmentsRegistry);
}
