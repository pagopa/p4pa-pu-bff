package it.gov.pagopa.pu.bff.connector.workflow_hub.config;

import it.gov.pagopa.pu.bff.config.rest.ApiClientConfig;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.workflow-hub")
@SuperBuilder
@NoArgsConstructor
public class WorkflowHubApiClientConfig extends ApiClientConfig {
}
