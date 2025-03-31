package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import it.gov.pagopa.pu.bff.config.CoreServiceHealthStatusConfig;
import it.gov.pagopa.pu.bff.config.MonitoringServiceConf;
import it.gov.pagopa.pu.bff.dto.generated.ServiceStatus;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
  classes = {CoreHealthIndicatorService.class, CoreHealthIndicatorServiceTest.CoreHealthIndicatorServiceTestConfiguration.class},
  webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableWireMock({
  @ConfigureWireMock(name = "monitoring-server", port = 8888)
})
class CoreHealthIndicatorServiceTest {

  @InjectWireMock(value = "monitoring-server")
  private static WireMockServer wireMockServer;

  @Autowired
  private CoreHealthIndicatorService coreHealthIndicatorService;

  @MockitoBean
  private MonitoringServiceConf monitoringServiceConf;

  @TestConfiguration
  public static class CoreHealthIndicatorServiceTestConfiguration {
    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
      return new RestTemplateBuilder();
    }
  }
  @Test
  void givenValidRequestWhenGetStatusThenOk() {
    // When
    Mockito.when(monitoringServiceConf.getServices()).thenReturn(Map.of(
      "P4PA", new CoreServiceHealthStatusConfig("P4PA", "http://localhost:8888/actuator/health/readiness")
    ));

    List<ServiceStatus> result = coreHealthIndicatorService.getStatus();

    // Then
    assertEquals(1, result.size());

    ServiceStatus status = result.getFirst();
    assertEquals("P4PA", status.getServiceName());
    assertEquals("\"UP\"", status.getStatusMessage());
  }
}
