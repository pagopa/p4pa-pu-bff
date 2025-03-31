package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreasuryViewFiltersDTO {

  private Long organizationId;
  private String iuv;
  private String iuf;
  private Long billAmountCents;
  private LocalDate billDate;
  private String provisionalCode;
  private String billCode;
  private String pspLastName;
  private LocalDate regionValueDate;
  private String documentCode;

}
