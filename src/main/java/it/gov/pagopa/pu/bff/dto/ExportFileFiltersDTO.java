package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportFileFiltersDTO {
  private Long organizationId;
  private FlowFileTypeEnum flowFileType;
  private OffsetDateTimeIntervalFilter creationDate;
  private ExportFileStatus status;
  private String fileName;
}
