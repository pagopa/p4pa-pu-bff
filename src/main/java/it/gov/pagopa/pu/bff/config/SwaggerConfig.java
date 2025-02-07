package it.gov.pagopa.pu.bff.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Class SwaggerConfig.
 */
@Configuration
@OpenAPIDefinition(
  info = @io.swagger.v3.oas.annotations.info.Info(
    title = "${spring.application.name}",
    version = "${spring.application.version}",
    description = "Api and Models"
  ),
  security = @SecurityRequirement(name = "BearerAuth")
)
@SecurityScheme(
  name = "bearerAuth",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
public class SwaggerConfig {

  /** The title. */
  private final String title;

  /** The description. */
  private final String description;

  /** The version. */
  private final String version;

  public SwaggerConfig(
    @Value("${swagger.title:${spring.application.name}}") String title,
    @Value("${swagger.description:Api and Models}") String description,
    @Value("${swagger.version:${spring.application.version}}") String version) {
    this.title = title;
    this.description = description;
    this.version = version;
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().components(new Components()).info(new Info()
        .title(title)
        .description(description)
        .version(version));
  }
}
