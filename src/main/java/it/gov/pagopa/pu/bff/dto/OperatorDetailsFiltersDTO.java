package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorDetailsFiltersDTO {
  private Long organizationId;
  private String mappedExternalUserId;
  private String debtPositionTypeOrgCode;
  private String debtPositionTypeOrgDescription;
  private Long debtPositionTypeId;
}
