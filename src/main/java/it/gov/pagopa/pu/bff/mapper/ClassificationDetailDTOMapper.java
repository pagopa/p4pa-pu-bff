package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassificationDetailDTOMapper {
  @Mapping(target = "paid", expression = "java(isPaid(classificationDetailViewDTO.getLabel(), classificationDetailViewDTO.getReceiptPaymentRequestId()))")
  @Mapping(target = "reported", expression = "java(isReported(classificationDetailViewDTO.getLabel()))")
  @Mapping(target = "collected", expression = "java(isCollected(classificationDetailViewDTO.getLabel()))")
  @Mapping(target = "status", expression = "java(ClassificationStatusMapper.mapStatus(classificationDetailViewDTO.getLabel(), classificationDetailViewDTO.getReceiptPaymentRequestId()))")
  @Mapping(target = "flagPaymentNotification", expression = "java(organization != null ? organization.getFlagPaymentNotification() : null)")
  @Mapping(target = "flagTreasury", expression = "java(organization != null ? organization.getFlagTreasury() : null)")
  ClassificationDetailDTO map(ClassificationDetailViewDTO classificationDetailViewDTO, @Context Organization organization);

  default boolean isPaid(ClassificationsEnum classification, String receiptPaymentRequestId) {
    return switch (classification) {
      case DOPPI, RT_NO_IUF, RT_NO_IUD, IUD_RT_IUF, RT_IUF, RT_TES,
           IUD_RT_IUF_TES, RT_IUF_TES, IUF_TES_DIV_IMP, IUD_NO_RT -> true;
      case IUF_NO_TES -> receiptPaymentRequestId != null;
      default -> false;
    };
  }

  default boolean isReported(ClassificationsEnum classification) {
    return switch (classification) {
      case DOPPI, IUF_NO_TES, IUD_RT_IUF, RT_IUF, RT_TES, IUD_RT_IUF_TES,
           RT_IUF_TES, IUF_TES_DIV_IMP, IUV_NO_RT -> true;
      default -> false;
    };
  }

  default boolean isCollected(ClassificationsEnum classification) {
    return switch (classification) {
      case DOPPI, TES_NO_IUF_OR_IUV, IUD_RT_IUF, IUD_RT_IUF_TES, RT_TES,
           RT_IUF_TES, IUF_TES_DIV_IMP, TES_NO_MATCH -> true;
      default -> false;
    };
  }
}

