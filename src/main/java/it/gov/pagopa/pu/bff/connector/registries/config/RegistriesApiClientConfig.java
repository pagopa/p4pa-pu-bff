package it.gov.pagopa.pu.bff.connector.registries.config;

import it.gov.pagopa.pu.bff.config.rest.ApiClientConfig;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.registries")
@SuperBuilder
@NoArgsConstructor
public class RegistriesApiClientConfig extends ApiClientConfig {
}
