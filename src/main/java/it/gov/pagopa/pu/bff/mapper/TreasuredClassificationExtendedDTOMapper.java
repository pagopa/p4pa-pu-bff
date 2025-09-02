package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.TreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationView;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TreasuredClassificationExtendedDTOMapper {

  @Mapping(
    target = "content",
    expression = "java(source.getContent() != null ? map(source.getContent(), organization) : java.util.Collections.emptyList())"
  )
  @Mapping(target = "totalPages", source = "totalPages")
  @Mapping(target = "size", source = "size")
  @Mapping(target = "number", source = "number")
  @Mapping(target = "totalElements", source = "totalElements")
  PagedTreasuredClassificationExtendedDTO map(PagedTreasuredClassification source, @Context Organization organization);

  List<TreasuredClassificationExtendedDTO> map(List<TreasuredClassificationView> source, @Context Organization organization);

  @Mapping(target = "status", expression = "java(ClassificationStatusMapper.mapStatus(source.getLabel()))")
  @Mapping(target = "flagPaymentNotification", expression = "java(organization.getFlagPaymentNotification())")
  @Mapping(target = "flagTreasury", expression = "java(organization.getFlagTreasury())")
  TreasuredClassificationExtendedDTO map(TreasuredClassificationView source, @Context Organization organization);
}

