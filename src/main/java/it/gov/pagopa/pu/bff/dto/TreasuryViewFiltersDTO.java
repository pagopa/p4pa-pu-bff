package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreasuryViewFiltersDTO {

  private Long organizationId;
  private String iuv;
  private String iuf;
  private Long billAmountCents;
  private LocalDateIntervalFilter billDateFilter;
  private String provisionalCode;
  private String provisionalAe;
  private String billCode;
  private String billYear;
  private String pspLastName;
  private LocalDateIntervalFilter regionValueDateFilter;
  private String documentCode;
  private String documentYear;

}
