package it.gov.pagopa.pu.bff.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.gov.pagopa.pu.bff.dto.generated.ServiceStatus;
import it.gov.pagopa.pu.bff.security.JwtAuthenticationFilter;
import it.gov.pagopa.pu.bff.service.CoreHealthIndicatorService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = CoreHealthIndicatorController.class, excludeFilters = {
  @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class CoreHealthIndicatorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CoreHealthIndicatorService coreHealthIndicatorService;

  @InjectMocks
  private CoreHealthIndicatorController coreHealthIndicatorController;

  private List<ServiceStatus> mockServiceStatuses;

  @BeforeEach
  void setUp() {
    mockServiceStatuses = Arrays.asList(
      new ServiceStatus("ServiceA", "UP"),
      new ServiceStatus("ServiceB", "DOWN")
    );
  }

  @Test
  void givenValidRequestWhenGetStatusThenOk() throws Exception {
    Mockito.when(coreHealthIndicatorService.getStatus()).thenReturn(mockServiceStatuses);

    mockMvc.perform(get("/bff/health")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(
          "[{\"serviceName\":\"ServiceA\",\"status\":\"UP\"},{\"serviceName\":\"ServiceB\",\"status\":\"DOWN\"}]"));
  }
}
