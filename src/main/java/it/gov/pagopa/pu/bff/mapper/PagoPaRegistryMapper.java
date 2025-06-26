package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPagoPaRegistry;
import it.gov.pagopa.pu.registries.dto.generated.PagedModelPagoPaRegistry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagoPaRegistryMapper {
  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? source.getEmbedded().getPagoPaRegistries() : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedPagoPaRegistry mapToPagedPagoPaRegistry(PagedModelPagoPaRegistry source);
}
