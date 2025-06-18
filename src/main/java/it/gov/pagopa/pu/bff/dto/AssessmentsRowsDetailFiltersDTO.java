package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentsRowsDetailFiltersDTO {
  private Long organizationId;
  private Long assessmentId;
  private String iud;
  private String iuv;
  private OffsetDateTime updateDateTimeFrom;
  private OffsetDateTime updateDateTimeTo;
  private OffsetDateTime paymentDateTimeFrom;
  private OffsetDateTime paymentDateTimeTo;
  private String fiscalCode;
}
