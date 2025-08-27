package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface AssessmentsRegistryExtendedDTOMapper {
  @Mapping(
          target = "content",
          expression = "java(source.getEmbedded() != null ? " +
                  "map(source.getEmbedded().getAssessmentsRegistries(), debtPositionTypeOrgMap) : " +
                  "java.util.Collections.emptyList())"
  )
  @Mapping(target = "totalPages", source = "source.page.totalPages")
  @Mapping(target = "size", source = "source.page.size")
  @Mapping(target = "number", source = "source.page.number")
  @Mapping(target = "totalElements", source = "source.page.totalElements")
  PagedAssessmentsRegistry mapToPagedAssessmentsRegistry(
          PagedModelAssessmentsRegistry source,
          @Context Map<String, DebtPositionTypeOrg> debtPositionTypeOrgMap
  );

  @Mapping(
          target = "debtPositionTypeOrgDescription",
          expression = "java(debtPositionTypeOrgMap!=null && debtPositionTypeOrgMap.get(source.getDebtPositionTypeOrgCode()) != null ? " +
                  "debtPositionTypeOrgMap.get(source.getDebtPositionTypeOrgCode()).getDescription() : null)"
  )
  AssessmentsRegistryExtendedDTO mapAssessmentsRegistry(AssessmentsRegistry source, @Context Map<String, DebtPositionTypeOrg> debtPositionTypeOrgMap);

  List<AssessmentsRegistryExtendedDTO> map(List<AssessmentsRegistry> source, @Context Map<String, DebtPositionTypeOrg> debtPositionTypeOrgMap);
}