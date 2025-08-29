package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.TreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TreasuredClassificationExtendedDTOMapper {

  PagedTreasuredClassificationExtendedDTO map(PagedTreasuredClassification source);

  List<TreasuredClassificationExtendedDTO> map(List<TreasuredClassificationView> source);

  @Mapping(target = "status", expression = "java(ClassificationStatusMapper.mapStatus(source.getLabel()))")
  TreasuredClassificationExtendedDTO map(TreasuredClassificationView source);
}

