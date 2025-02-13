package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportFileFiltersDTO {
  private Long organizationId;
  private FlowFileTypeEnum flowFileType;
  private OffsetDateTime creationDateFrom;
  private OffsetDateTime creationDateTo;
  private String status;
  private String fileName;
}
