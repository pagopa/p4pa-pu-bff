package it.gov.pagopa.pu.bff.dto;

import it.gov.pagopa.pu.registries.dto.generated.RegistryPagoPaEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagoPaRegistryFiltersDTO {
  private RegistryPagoPaEventType eventType;
  private OffsetDateTimeIntervalFilter eventDate;
  private String iuv;
}
