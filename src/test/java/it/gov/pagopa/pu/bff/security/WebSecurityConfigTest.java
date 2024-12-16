package it.gov.pagopa.pu.bff.security;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.gov.pagopa.pu.bff.controller.generated.BrokersApi;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.organization.BrokerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(value = {BrokersApi.class},includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
  classes = JwtAuthenticationFilter.class) )
@Import(WebSecurityConfig.class)
class WebSecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private BrokerServiceImpl serviceMock;

  @MockBean
  private AuthorizationService authorizationServiceMock;

  @Test
  void givenURLWhenWithoutAccessTokenThenReturn403() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/notFound"))
      .andExpect(status().is4xxClientError());
  }

}
