package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpstreamErrorDTO {
  private String code;
  private String message;
}
