package it.gov.pagopa.pu.bff.config;

import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.default-config-fe")
public class DefaultConfigFe extends PersonalisationFe {

}
