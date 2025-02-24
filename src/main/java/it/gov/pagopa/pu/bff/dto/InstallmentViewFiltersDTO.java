package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentViewFiltersDTO {

  private Long organizationId;
  private String operatorExternalUserId;
  private OffsetDateTimeIntervalFilter dueDate;
  private String iuv;
  private String fiscalCode;
  private Long debtPositionTypeOrgId;

}
