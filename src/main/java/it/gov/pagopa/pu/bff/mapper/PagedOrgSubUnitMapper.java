package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagedOrgSubUnitMapper {
  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? source.getEmbedded().getOrgSubUnits() : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedOrgSubUnit map(PagedModelOrgSubUnit source);
}
