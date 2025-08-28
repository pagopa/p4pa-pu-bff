package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtPositionViewFiltersDTO {
  private Long organizationId;
  private OffsetDateTime creationDateTimeFrom;
  private OffsetDateTime creationDateTimeTo;
  private String fiscalCode;
  private Long debtPositionTypeOrgId;
  private DebtPositionStatus status;
}
