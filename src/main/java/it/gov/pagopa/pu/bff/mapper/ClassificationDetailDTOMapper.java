package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassificationDetailDTOMapper {
  @Mapping(target = "payed", expression = "java(isPayed(classificationDetailViewDTO.getLabel()))")
  @Mapping(target = "reported", expression = "java(isReported(classificationDetailViewDTO.getLabel()))")
  @Mapping(target = "collected", expression = "java(isCollected(classificationDetailViewDTO.getLabel()))")
  ClassificationDetailDTO map(ClassificationDetailViewDTO classificationDetailViewDTO);

  default boolean isPayed(ClassificationsEnum classification) {
    return switch (classification) {
      case DOPPI, RT_NO_IUF, RT_NO_IUD, IUV_NO_RT, IUD_RT_IUF, RT_IUF, RT_TES,
           IUD_RT_IUF_TES, RT_IUF_TES, IUF_TES_DIV_IMP, IUD_NO_RT -> true;
      default -> false;
    };
  }

  default boolean isReported(ClassificationsEnum classification) {
    return switch (classification) {
      case DOPPI, IUF_NO_TES, IUD_RT_IUF, RT_IUF, RT_TES, RT_IUF_TES,
           IUF_TES_DIV_IMP, IUD_NO_RT -> true;
      default -> false;
    };
  }

  default boolean isCollected(ClassificationsEnum classification) {
    return switch (classification) {
      case DOPPI, TES_NO_IUF_OR_IUV, IUD_RT_IUF, RT_TES, RT_IUF_TES,
           IUF_TES_DIV_IMP, TES_NO_MATCH -> true;
      default -> false;
    };
  }
}

