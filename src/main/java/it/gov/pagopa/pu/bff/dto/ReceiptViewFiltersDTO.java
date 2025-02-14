package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptViewFiltersDTO {

  private Long organizationId;
  private ReceiptView.ReceiptOriginEnum receiptOrigin;
  private String operatorExternalUserId;
  private String iuv;
  private String iur;
  private String iud;
  private Long debtPositionTypeOrgId;
  private OffsetDateTimeIntervalFilter paymentDateTime;

}
