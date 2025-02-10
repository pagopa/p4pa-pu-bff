package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptViewFiltersDTO {
  private Long organizationId;
  private String receiptOrigin;
  private String operatorExternalUserId;
  private String iuv;
  private String iur;
  private String iud;
  private Long debtPositionTypeOrgId;
  private OffsetDateTime fromDate;
  private OffsetDateTime toDate;
}
