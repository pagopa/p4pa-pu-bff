package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.registries.dto.generated.RegistryOutcome;
import it.gov.pagopa.pu.registries.dto.generated.RegistrySilEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SilRegistryFiltersDTO {
  private RegistrySilEventType eventType;
  private OffsetDateTimeIntervalFilter eventDate;
  private String iuv;
  private RegistryOutcome outcome;
}
