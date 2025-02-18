package it.gov.pagopa.pu.bff.connector.classification.config;

import it.gov.pagopa.pu.bff.config.ApiClientConfig;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.classification")
@SuperBuilder
@NoArgsConstructor
public class ClassificationApiClientConfig extends ApiClientConfig {
}
