package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptViewFiltersDTO {

  private Long organizationId;
  private List<ReceiptOriginType> receiptOrigins;
  private String operatorExternalUserId;
  private String iuv;
  private String iur;
  private String iud;
  private Long debtPositionTypeOrgId;
  private OffsetDateTimeIntervalFilter paymentDateTime;

}
