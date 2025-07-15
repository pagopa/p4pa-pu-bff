package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationPaidInstallmentsFiltersDTO {
  private String iuv;
  private OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter;
  private OffsetDateTimeIntervalFilter updateDateIntervalFilter;
  private String debtPositionTypeOrgCode;
  private Set<String> iuds;
}
