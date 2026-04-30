package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentsRowsDetailFiltersDTO {
  private Long organizationId;
  private Long assessmentId;
  private String iud;
  private String iuv;
  private LocalDateTimeIntervalFilter updateDateTimeIntervalFilter;
  private OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter;
  private String fiscalCode;
}
