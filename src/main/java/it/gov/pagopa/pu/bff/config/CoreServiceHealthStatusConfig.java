package it.gov.pagopa.pu.bff.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoreServiceHealthStatusConfig {
    private String serviceName;
    private String url;
}
