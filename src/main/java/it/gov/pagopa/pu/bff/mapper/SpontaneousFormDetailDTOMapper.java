package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.SpontaneousFormDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpontaneousFormDetailDTOMapper {
  SpontaneousFormDetailDTO map(SpontaneousForm source);
}
