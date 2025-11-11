package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionOrigin;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class InstallmentViewFiltersDTO {

  private Long organizationId;
  private String operatorExternalUserId;
  private LocalDateIntervalFilter dueDate;
  private String iuv;
  private String iud;
  private String fiscalCode;
  private List<DebtPositionOrigin> debtPositionOrigins;
  private Long debtPositionTypeOrgId;

}
