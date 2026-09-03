package it.gov.pagopa.pu.bff.exception;

import it.gov.pagopa.pu.bff.exception.common.CommonExceptionHandlerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.doThrow;

class GlobalExceptionHandlerTest extends CommonExceptionHandlerTest {

  @Test
  void handleInvalidDebtPositionException() throws Exception {
    doThrow(new InvalidDebtPositionException("TYPE_IN_USE", "Bad Request: Debt Position ID should not be provided"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("TYPE_IN_USE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request: Debt Position ID should not be provided"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidDebtPositionTypeOrgException() throws Exception {
    doThrow(new InvalidDebtPositionTypeOrgException("TYPE_ORG_ERROR", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("TYPE_ORG_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOperatorRoleException() throws Exception {
    doThrow(new InvalidOperatorRoleException("INVALID_OPERATOR_ROLE", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_OPERATOR_ROLE"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleZipFileException() throws Exception {
    doThrow(new ZipFileException("ZIPPING_ERROR", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("GENERIC_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ZIPPING_ERROR"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidAssessmentsRegistryException() throws Exception {
    doThrow(new InvalidAssessmentsRegistryException("ASSESSMENT_REGISTRY_GENERIC", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ASSESSMENT_REGISTRY_GENERIC"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidAssessmentsDetailException() throws Exception {
    doThrow(new InvalidAssessmentsDetailException("INVALID_ASSESSMENT_DETAIL", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ASSESSMENT_DETAIL"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOrganizationException() throws Exception {
    doThrow(new InvalidOrganizationException("INVALID_ORGANIZATION", "Error"))
      .when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ORGANIZATION"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOrgSilServiceException() throws Exception {
    doThrow(new InvalidOrgSilServiceException("ORG_SIL_SERVICE_ALREADY_EXISTS", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("ORG_SIL_SERVICE_ALREADY_EXISTS"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidAccessTokenException() throws Exception {
    doThrow(new InvalidAccessTokenException("INVALID_ACCESS_TOKEN", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ACCESS_TOKEN"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidUserInfoException() throws Exception {
    doThrow(new InvalidUserInfoException("INVALID_USER_INFO", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_USER_INFO"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidOrgSubUnitException() throws Exception {
    doThrow(new InvalidOrgSubUnitException("INVALID_ORG_SUB_UNIT", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_ORG_SUB_UNIT"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }

  @Test
  void handleInvalidPdndClientException() throws Exception {
    doThrow(new InvalidPdndClientException("INVALID_PDND_CLIENT", "Error")).when(testControllerSpy).testEndpoint(DATA, BODY);

    performRequest(DATA, MediaType.APPLICATION_JSON)
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BAD_REQUEST"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("INVALID_PDND_CLIENT"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.fields").doesNotExist())
      .andExpect(MockMvcResultMatchers.jsonPath("$.traceId").value(traceId));
  }
}
