package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionView.StatusEnum;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtPositionViewFiltersDTO {
  private Long organizationId;
  private OffsetDateTime creationDateFrom;
  private OffsetDateTime creationDateTo;
  private String fiscalCode;
  private Long debtPositionTypeOrgId;
  private StatusEnum status;
}
