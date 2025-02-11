package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportFileFiltersDTO {
  private Long organizationId;
  private List<FlowFileTypeEnum> flowFileTypes;
  private OffsetDateTime creationDateFrom;
  private OffsetDateTime creationDateTo;
  private String status;
  private String fileName;
}
