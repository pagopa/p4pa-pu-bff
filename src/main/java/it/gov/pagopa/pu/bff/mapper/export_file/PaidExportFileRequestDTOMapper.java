package it.gov.pagopa.pu.bff.mapper.export_file;

import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequest;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PaidExportFileRequestDTOMapper {

  public PaidExportFileRequestDTO map2ProcessExecutionsDto(
    PaidExportFileRequest paidExportFileRequest) {
    PaidExportFileFilter filterFields = PaidExportFileFilter.builder()
      .paymentDateTime(paidExportFileRequest.getFilterFields().getPaymentDate() != null ?
        DateUtils.toRangeClosedOffsetDateTimeIntervalFilter(paidExportFileRequest.getFilterFields().getPaymentDate())
        : null)
      .debtPositionTypeOrgId(paidExportFileRequest.getFilterFields().getDebtPositionTypeOrgId() != null ?
        paidExportFileRequest.getFilterFields().getDebtPositionTypeOrgId()
        : null)
      .build();

    return PaidExportFileRequestDTO.builder()
      .organizationId(paidExportFileRequest.getOrganizationId())
      .exportFileType(paidExportFileRequest.getExportFileType())
      .fileVersion(paidExportFileRequest.getFileVersion())
      .filterFields(filterFields).build();
  }

}
