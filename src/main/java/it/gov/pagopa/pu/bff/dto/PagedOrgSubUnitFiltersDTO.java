package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitStatus;
import it.gov.pagopa.pu.organization.dto.generated.SubUnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedOrgSubUnitFiltersDTO {
  private Long organizationId;
  private String mappedExternalUserId;
  private String subUnitCode;
  private OrgSubUnitStatus status;
  private SubUnitType subUnitType;
}
