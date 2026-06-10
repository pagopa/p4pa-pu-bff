package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface PagedDebtPositionTypeOrgDTOMapper {

  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? map(source.getEmbedded().getDebtPositionTypeOrgs(),debtPositionTypes) : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedDebtPositionTypeOrgDTO map(PagedModelDebtPositionTypeOrg source, @Context Map<Long, DebtPositionType> debtPositionTypes);

  List<DebtPositionTypeOrgDTO> map(List<DebtPositionTypeOrg> content, @Context Map<Long, DebtPositionType> debtPositionTypes);

  default DebtPositionTypeOrgDTO map(DebtPositionTypeOrg dpto, @Context Map<Long, DebtPositionType> debtPositionTypes) {
    return Mappers.getMapper(DebtPositionTypeOrgDTOMapper.class).map(
            dpto,
            debtPositionTypes != null ? debtPositionTypes.get(dpto.getDebtPositionTypeId()) : null,
            null,
            null,
            null,
            Collections.emptyList()
    );
  }
}
