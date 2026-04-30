package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSilServiceView;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSilServiceView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrgSilServiceViewMapper {
  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? source.getEmbedded().getOrgSilServiceViews() : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedOrgSilServiceView map(PagedModelOrgSilServiceView source);
}
