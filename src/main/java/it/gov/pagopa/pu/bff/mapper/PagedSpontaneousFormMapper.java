package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagedSpontaneousFormMapper {

  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? source.getEmbedded().getSpontaneousForms() : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedSpontaneousForm map(PagedModelSpontaneousForm source);
}
