package it.gov.pagopa.pu.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.controller.generated.OrganizationsApi;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationDTO;
import it.gov.pagopa.pu.bff.security.JwtAuthenticationFilter;
import it.gov.pagopa.pu.bff.service.organization.OrganizationServiceImpl;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OrganizationsApi.class, excludeFilters = @ComponentScan.Filter(
  type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class OrganizationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrganizationServiceImpl organizationServiceMock;

  private List<OrganizationDTO> organizationDTOList;

  @BeforeEach
  void setUp() {
    organizationDTOList = new ArrayList<>();
  }

  @Test
  void testGetOrganizations() throws Exception {
    TestUtils.addSampleUserIntoSecurityContext();

    Mockito.when(organizationServiceMock.getOrganizations(TestUtils.getSampleUser(), "fakeAccessToken"))
      .thenReturn(organizationDTOList);

    MvcResult result = mockMvc.perform(get("/bff/organizations")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

    Assertions.assertEquals(result.getResponse().getContentAsString(), objectMapper.writeValueAsString(organizationDTOList));
  }

}
