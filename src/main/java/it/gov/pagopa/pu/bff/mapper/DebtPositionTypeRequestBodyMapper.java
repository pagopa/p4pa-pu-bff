package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class DebtPositionTypeRequestBodyMapper {

  public it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody map (DebtPositionTypeRequestBody dto, Long brokerId){
    return map(dto)
      .brokerId(brokerId);
  }

  @Mapping(target = "brokerId", constant = "-1L")
  protected abstract it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody map (DebtPositionTypeRequestBody dto);
}
