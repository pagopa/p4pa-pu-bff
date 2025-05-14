package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileFilter;
import org.springframework.stereotype.Component;

@Component
public class ExportFileRequestDTOMapper {

  public it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO map2ProcessExecutionsDto(
    PaidExportFileRequestDTO paidExportFileRequestDTO) {
    PaidExportFileFilter filterFields = PaidExportFileFilter.builder()
      .paymentDateTime(getOffsetDateTimeIntervalFilter(
        paidExportFileRequestDTO.getPaymentDate()))
      .debtPositionTypeOrgId(
        paidExportFileRequestDTO.getDebtPositionTypeOrgId())
      .build();

    return it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO.builder()
      .organizationId(paidExportFileRequestDTO.getOrganizationId())
      .exportFileType(
        it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO.ExportFileTypeEnum.valueOf(
          paidExportFileRequestDTO.getExportFileType().name()))
      .fileVersion(paidExportFileRequestDTO.getFileVersion())
      .filterFields(filterFields)
      .build();
  }

  public it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO map2ProcessExecutionsDto(
    ReceiptsArchivingExportFileRequestDTO receiptsArchivingExportFileRequestDTO) {
    ReceiptsArchivingExportFileFilter filterFields = ReceiptsArchivingExportFileFilter.builder()
      .paymentDateTime(getOffsetDateTimeIntervalFilter(
        receiptsArchivingExportFileRequestDTO.getPaymentDate()))
      .build();

    return it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO.builder()
      .organizationId(receiptsArchivingExportFileRequestDTO.getOrganizationId())
      .exportFileType(
        it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO.ExportFileTypeEnum.valueOf(
          receiptsArchivingExportFileRequestDTO.getExportFileType().name()))
      .fileVersion(receiptsArchivingExportFileRequestDTO.getFileVersion())
      .filterFields(filterFields)
      .build();
  }

  private OffsetDateTimeIntervalFilter getOffsetDateTimeIntervalFilter(
    LocalDateIntervalFilter localDateIntervalFilter) {
    return OffsetDateTimeIntervalFilter.builder()
      .from(DateUtils.toOffsetDateTimeStartOfTheDay(
        localDateIntervalFilter.getFrom()))
      .to(DateUtils.toOffsetDateTimeStartOfTheDay(
        localDateIntervalFilter.getTo()))
      .build();
  }

}
