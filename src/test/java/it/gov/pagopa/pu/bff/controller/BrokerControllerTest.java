package it.gov.pagopa.pu.bff.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.controller.generated.BrokersApi;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.security.JwtAuthenticationFilter;
import it.gov.pagopa.pu.bff.service.organization.BrokerServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(value = BrokersApi.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
  classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class BrokerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BrokerServiceImpl serviceMock;

  private ConfigFE configFE;

  @BeforeEach
  void setUp() {
    configFE = new ConfigFE();
  }

  @Test
  void testGetBrokerConf() throws Exception {

    TestUtils.addSampleUserIntoSecurityContext();
    Mockito.when(serviceMock.getBrokerConfig(TestUtils.getSampleUser(),"token")).thenReturn(configFE);
    MvcResult result = mockMvc.perform(get("/bff/brokers/config"))
      .andExpect(status().isOk())
      .andReturn();
    Assertions.assertEquals(result.getResponse().getContentAsString(),objectMapper.writeValueAsString(configFE));
  }
}
