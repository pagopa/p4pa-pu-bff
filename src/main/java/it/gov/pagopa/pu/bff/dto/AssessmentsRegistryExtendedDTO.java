package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
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
public class AssessmentsRegistryExtendedDTO extends AssessmentsRegistry {

  private String debtPositionTypeOrgDescription;
}
