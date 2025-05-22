package it.gov.pagopa.pu.bff.mapper.export_file;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequest;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class ReceiptsArchivingExportFileRequestDTOMapper {

  public ReceiptsArchivingExportFileRequestDTO map2ProcessExecutionsDto(
    ReceiptsArchivingExportFileRequest receiptsArchivingExportFileRequest) {
    ReceiptsArchivingExportFileFilter filterFields = ReceiptsArchivingExportFileFilter.builder()
      .paymentDateTime(receiptsArchivingExportFileRequest.getFilterFields().getPaymentDate() != null ?
        DateUtils.toRangeClosedOffsetDateTimeIntervalFilter(receiptsArchivingExportFileRequest.getFilterFields().getPaymentDate())
        : null)
      .build();

    return ReceiptsArchivingExportFileRequestDTO.builder()
      .organizationId(receiptsArchivingExportFileRequest.getOrganizationId())
      .exportFileType(receiptsArchivingExportFileRequest.getExportFileType())
      .fileVersion(receiptsArchivingExportFileRequest.getFileVersion())
      .filterFields(filterFields)
      .build();
  }

}
