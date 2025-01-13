package it.gov.pagopa.pu.bff.config;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "monitoring")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringServiceConf {
  private Map<String, CoreServiceHealthStatusConfig> services;
}
