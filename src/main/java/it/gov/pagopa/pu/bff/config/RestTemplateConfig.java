package it.gov.pagopa.pu.bff.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {
  private final int connectTimeoutMillis;
  private final int readTimeoutHandlerMillis;

  public RestTemplateConfig(
    @Value("${app.rest-client.connect.timeout.millis}") int connectTimeoutMillis,
    @Value("${app.rest-client.read.timeout.millis}") int readTimeoutHandlerMillis) {
    this.connectTimeoutMillis = connectTimeoutMillis;
    this.readTimeoutHandlerMillis = readTimeoutHandlerMillis;
  }

  @Bean
  public RestTemplateBuilder restTemplateBuilder(
    RestTemplateBuilderConfigurer configurer) {
    return configurer.configure(new RestTemplateBuilder())
      .setConnectTimeout(Duration.ofMillis(connectTimeoutMillis))
      .setReadTimeout(Duration.ofMillis(readTimeoutHandlerMillis));
  }
}
