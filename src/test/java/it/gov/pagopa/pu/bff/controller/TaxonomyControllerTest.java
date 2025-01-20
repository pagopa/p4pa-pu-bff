package it.gov.pagopa.pu.bff.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.controller.generated.TaxonomyApi;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.bff.security.JwtAuthenticationFilter;
import it.gov.pagopa.pu.bff.service.broker.BrokerService;
import it.gov.pagopa.pu.bff.service.taxonomy.TaxonomyService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(value = TaxonomyApi.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
  classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class TaxonomyControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private TaxonomyService serviceMock;

  @MockitoBean
  private BrokerService serviceBrokerMock;

  @Test
  void testGetCollectionReason() throws Exception {
    TestUtils.addSampleUserIntoSecurityContext();
    List<TaxonomyCollectionReasonDTO> res = new ArrayList<>();
    Mockito.when(serviceMock.getCollectionReason("organizationType","macroAreaCode","serviceTypeCode","token")).thenReturn(res);
    MvcResult result = mockMvc.perform(get("/bff/taxonomy/getCollectionReason")
        .queryParam("organizationType","organizationType")
        .queryParam("macroAreaCode","macroAreaCode")
        .queryParam("serviceTypeCode","serviceTypeCode"))
      .andExpect(status().isOk())
      .andReturn();
    Assertions.assertEquals(result.getResponse().getContentAsString(),objectMapper.writeValueAsString(res));
  }

  @Test
  void testGetMacroArea() throws Exception {
    TestUtils.addSampleUserIntoSecurityContext();
    List<TaxonomyMacroAreaCodeDTO> res = new ArrayList<>();
    Mockito.when(serviceMock.getMacroArea("organizationType","token")).thenReturn(res);
    MvcResult result = mockMvc.perform(get("/bff/taxonomy/getMacroArea")
        .queryParam("organizationType","organizationType"))
      .andExpect(status().isOk())
      .andReturn();
    Assertions.assertEquals(result.getResponse().getContentAsString(),objectMapper.writeValueAsString(res));
  }

  @Test
  void testGetOrganizationTypes() throws Exception {
    TestUtils.addSampleUserIntoSecurityContext();
    List<TaxonomyOrganizationTypeDTO> res = new ArrayList<>();
    Mockito.when(serviceMock.getOrganizationTypes("token")).thenReturn(res);
    MvcResult result = mockMvc.perform(get("/bff/taxonomy/getOrganizationTypes"))
      .andExpect(status().isOk())
      .andReturn();
    Assertions.assertEquals(result.getResponse().getContentAsString(),objectMapper.writeValueAsString(res));
  }

  @Test
  void testGetServiceType() throws Exception {
    TestUtils.addSampleUserIntoSecurityContext();
    List<TaxonomyServiceTypeCodeDTO> res = new ArrayList<>();
    Mockito.when(serviceMock.getServiceType("organizationType","macroAreaCode","token")).thenReturn(res);
    MvcResult result = mockMvc.perform(get("/bff/taxonomy/getServiceType")
        .queryParam("organizationType","organizationType")
        .queryParam("macroAreaCode","macroAreaCode"))
      .andExpect(status().isOk())
      .andReturn();
    Assertions.assertEquals(result.getResponse().getContentAsString(),objectMapper.writeValueAsString(res));
  }

  @Test
  void testGetTaxonomyCode() throws Exception {
    TestUtils.addSampleUserIntoSecurityContext();
    List<TaxonomyCodeDTO> res = new ArrayList<>();
    Mockito.when(serviceMock.getTaxonomyCode("organizationType","macroAreaCode","serviceTypeCode","collectionReason","token")).thenReturn(res);
    MvcResult result = mockMvc.perform(get("/bff/taxonomy/getTaxonomyCode")
        .queryParam("organizationType","organizationType")
        .queryParam("macroAreaCode","macroAreaCode")
        .queryParam("serviceTypeCode","serviceTypeCode")
        .queryParam("collectionReason","collectionReason"))
      .andExpect(status().isOk())
      .andReturn();
    Assertions.assertEquals(result.getResponse().getContentAsString(),objectMapper.writeValueAsString(res));
  }

}
