package it.gov.pagopa.pu.bff.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngestionFlowFileFiltersDTO {
  private Long organizationId;
  private String flowFileType;
  private OffsetDateTime creationDateFrom;
  private OffsetDateTime creationDateTo;
  private String status;
  private String fileName;
}
