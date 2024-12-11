package it.gov.pagopa.pu.bff.security;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@Import(WebSecurityConfig.class)
class WebSecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void givenURLWhenWithoutAccessTokenThenRedirectToLogin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/actuator"))
      .andExpect(status().is(200));
  }

}
