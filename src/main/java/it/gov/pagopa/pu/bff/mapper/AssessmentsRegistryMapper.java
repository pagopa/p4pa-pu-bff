package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssessmentsRegistryMapper {
  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? source.getEmbedded().getAssessmentsRegistries() : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedAssessmentsRegistry mapToPagedAssessmentsRegistry(PagedModelAssessmentsRegistry source);
}
