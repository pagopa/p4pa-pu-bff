package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClassificationDetailDTO extends ClassificationDetailViewDTO {
  private boolean payed;
  private boolean reported;
  private boolean collected;
  private String status;
  private Boolean flagPaymentNotification;
  private Boolean flagTreasury;
}
