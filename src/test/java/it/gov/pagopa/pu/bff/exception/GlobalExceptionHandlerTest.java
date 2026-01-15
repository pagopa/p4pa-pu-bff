package it.gov.pagopa.pu.bff.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.mapper.UpstreamErrorMapper;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.bff.util.UtilitiesTest;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith({SpringExtension.class})
@WebMvcTest(value = {GlobalExceptionHandlerTest.TestController.class})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
  GlobalExceptionHandlerTest.TestController.class,
  GlobalExceptionHandler.class,
  JsonConfig.class})
class GlobalExceptionHandlerTest {

  public static final String DATA = "data";
  public static final TestRequestBody BODY = new TestRequestBody("bodyData", null, "abc", LocalDateTime.now());

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockitoBean
  private UpstreamErrorMapper upstreamErrorMapper;
  @MockitoSpyBean
  private TestController testControllerSpy;
  @MockitoSpyBean
  private RequestMappingHandlerAdapter requestMappingHandlerAdapterSpy;

  @RestController
  @Slf4j
  static class TestController {
    @PostMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    String testEndpoint(@RequestParam(DATA) String data, @Valid @RequestBody TestRequestBody body) {
      return "OK";
    }
  }

  @BeforeEach
  void init() {
    TestUtils.clearDefaultTimezone();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TestRequestBody {
    @NotNull
    private String requiredField;
    private String notRequiredField;
    @Pattern(regexp = "[a-z]+")
    private String lowerCaseAlphabeticField;
    private LocalDateTime dateTimeField;
  }

  private final String traceId = "TRACEID";
  @BeforeEach
  void setTraceId(){
    UtilitiesTest.setTraceId(traceId);
  }
  @AfterEach
  void clearTraceId(){
    UtilitiesTest.clearTraceIdContext();
  }

  private ResultActions performRequest(String data, MediaType accept) throws Exception {
    return performRequest(data, accept, objectMapper.writeValueAsString(GlobalExceptionHandlerTest.BODY));
  }

  private ResultActions performRequest(String data, MediaType accept, String body) throws Exception {
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/test")
      .param(DATA, data)
      .accept(accept);

    if (body != null) {
      requestBuilder
        .contentType(MediaType.APPLICATION_JSON)
        .content(body);
    }

    return mockMvc.perform(requestBuilder);
  }

  @Test
  void handleInvalidDebtPositionException() throws Exception {
    doThrow(new InvalidDebtPositionException("DEBT_POSITION_TYPE_IN_USE", "Bad Request: Debt Position ID should not be provided"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Bad Request: Debt Position ID should not be provided"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("DEBT_POSITION_TYPE_IN_USE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleMissingServletRequestParameterException() throws Exception {

    performRequest(null, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Required request parameter 'data' for method parameter type String is not present"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));

  }

  @Test
  void handleRuntimeExceptionError() throws Exception {
    doThrow(new RuntimeException("Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleGenericServletException() throws Exception {
    doThrow(new ServletException("Error"))
      .when(requestMappingHandlerAdapterSpy).handle(any(), any(), any());

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handle4xxHttpServletException() throws Exception {
    performRequest(DATA, MediaType.parseMediaType("application/hal+json"))
      .andExpect(MockMvcResultMatchers.status().isNotAcceptable())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("No acceptable representation"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleUrlNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/NOTEXISTENTURL"))
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("NOT_FOUND"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("No static resource NOTEXISTENTURL for request '/NOTEXISTENTURL'."))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleNoBodyException() throws Exception {
    performRequest(DATA, MediaType.APPLICATION_JSON, null)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Required request body is missing"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidBodyException() throws Exception {
    performRequest(DATA, MediaType.APPLICATION_JSON,
      "{\"notRequiredField\":\"notRequired\",\"lowerCaseAlphabeticField\":\"ABC\"}")
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(allOf(
        containsString("Invalid request content."),
        containsString("lowerCaseAlphabeticField:"),
        containsString("must match"),
        containsString("requiredField: must not be null")
      )))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleNotParsableBodyException() throws Exception {
    performRequest(DATA, MediaType.APPLICATION_JSON,
      "{\"notRequiredField\":\"notRequired\",\"dateTimeField\":\"2025-02-05\"}")
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Cannot parse body. dateTimeField: Text '2025-02-05' could not be parsed at index 10"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handle5xxHttpServletException() throws Exception {
    doThrow(new ServerErrorException("Error", new RuntimeException("Error")))
      .when(requestMappingHandlerAdapterSpy).handle(any(), any(), any());

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("500 INTERNAL_SERVER_ERROR \"Error\""))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  private final ConstraintViolationException constraintViolationException = new ConstraintViolationException("Error", Set.of(ConstraintViolationImpl.forParameterValidation(
    "error message template", Map.of(), Map.of(), "resolved message", null, null, null, null, PathImpl.createPathFromString("fieldName"), null, null, null
  )));

  @Test
  void handleViolationException() throws Exception {
    doThrow(constraintViolationException).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Invalid request content. fieldName: resolved message"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleAuthorizationDeniedException() throws Exception {
    doThrow(new AuthorizationDeniedException("Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isForbidden())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("FORBIDDEN"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleConflictException() throws Exception {
    doThrow(new ConflictException("DEBT_POSITION_TYPE_IN_USE", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isConflict())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("CONFLICT"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("DEBT_POSITION_TYPE_IN_USE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleResourceNotFoundException() throws Exception {
    doThrow(new ResourceNotFoundException("DEBT_POSITION_NOT_FOUND", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("NOT_FOUND"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("DEBT_POSITION_NOT_FOUND"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleHttpClientErrorException() throws Exception {
    String upstreamBody = """
    {"code":"SOME_UPSTREAM_CODE","message":"[INVALID_IBAN] eltjhreigjpo","traceId":"slfjhdio"}
    """;

    HttpClientErrorException ex = HttpClientErrorException.create(
      HttpStatus.BAD_REQUEST,
      "Error",
      HttpHeaders.EMPTY,
      upstreamBody.getBytes(StandardCharsets.UTF_8),
      StandardCharsets.UTF_8
    );

    doThrow(ex).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_IBAN"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("eltjhreigjpo"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidDebtPositionTypeOrgException() throws Exception {
    doThrow(new InvalidDebtPositionTypeOrgException("DEBT_POSITION_TYPE_ORG_ERROR", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("DEBT_POSITION_TYPE_ORG_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOperatorRoleException() throws Exception {
    doThrow(new InvalidOperatorRoleException("INVALID_OPERATOR_ROLE", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_OPERATOR_ROLE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleZipFileException() throws Exception {
    doThrow(new ZipFileException("ZIPPING_ERROR", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ZIPPING_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidAssessmentsRegistryException() throws Exception {
    doThrow(new InvalidAssessmentsRegistryException("ASSESSMENT_REGISTRY_GENERIC", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ASSESSMENT_REGISTRY_GENERIC"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidAssessmentsDetailException() throws Exception {
    doThrow(new InvalidAssessmentsDetailException("INVALID_ASSESSMENT_DETAIL", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ASSESSMENT_DETAIL"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOrganizationException() throws Exception {
    doThrow(new InvalidOrganizationException("INVALID_ORGANIZATION", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ORGANIZATION"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOrgSilServiceException() throws Exception {
    doThrow(new InvalidOrgSilServiceException("ORG_SIL_SERVICE_ALREADY_EXISTS", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ORG_SIL_SERVICE_ALREADY_EXISTS"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidAccessTokenException() throws Exception {
    doThrow(new InvalidAccessTokenException("INVALID_ACCESS_TOKEN", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ACCESS_TOKEN"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidUserInfoException() throws Exception {
    doThrow(new InvalidUserInfoException("INVALID_USER_INFO", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_USER_INFO"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }
}
