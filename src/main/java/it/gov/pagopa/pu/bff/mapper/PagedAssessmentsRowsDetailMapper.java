package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagedAssessmentsRowsDetailMapper {

  @Mapping(target = "content", expression = "java(source.getEmbedded() != null ? source.getEmbedded().getAssessmentsDetails() : java.util.Collections.emptyList())")
  @Mapping(target = "totalPages", source = "page.totalPages")
  @Mapping(target = "size", source = "page.size")
  @Mapping(target = "number", source = "page.number")
  @Mapping(target = "totalElements", source = "page.totalElements")
  PagedAssessmentsRowsDetail map(PagedModelAssessmentsDetail source);
}
