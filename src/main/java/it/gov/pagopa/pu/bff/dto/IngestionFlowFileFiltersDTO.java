package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.IngestionFlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngestionFlowFileFiltersDTO {
  private Long organizationId;
  private List<IngestionFlowFileTypeEnum> ingestionFlowFileType;
  private OffsetDateTime creationDateFrom;
  private OffsetDateTime creationDateTo;
  private IngestionFlowFileStatus status;
  private String fileName;
}
