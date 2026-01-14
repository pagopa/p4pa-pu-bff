package it.gov.pagopa.pu.bff.connector.organization.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.controller.generated.OrganizationApi;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationErrorDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
class OrganizationClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrganizationApi organizationApiMock;
  @Mock
  private HttpClientErrorException.BadRequest badRequestMock;
  @Mock
  private ObjectMapper objectMapperMock;

  private OrganizationClient organizationClient;

  @BeforeEach
  void setUp() {
    organizationClient = new OrganizationClient(organizationApisHolderMock, objectMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
            organizationApisHolderMock,
            organizationApiMock,
            badRequestMock
    );
  }

  @Test
  void givenExistingOrganizationWhenUpdateOrganizationThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    OrganizationDetailDTO organizationDetailDTO = new OrganizationDetailDTO();

    Mockito.when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    Mockito.doNothing().when(organizationApiMock).updateOrganization(organizationDetailDTO);

    organizationClient.updateOrganization(organizationDetailDTO, accessToken);

    Mockito.verifyNoMoreInteractions(organizationApisHolderMock,organizationApiMock);
  }

  @Test
  void givenNoExistentOrganizationWhenUpdateOrganizationThenResourceNotFoundException() {
    OrganizationDetailDTO organizationDetailDTO = new OrganizationDetailDTO();
    String accessToken = "ACCESSTOKEN";

    Mockito.when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    Mockito.doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null))
            .when(organizationApiMock).updateOrganization(organizationDetailDTO);

    Assertions.assertThrows(ResourceNotFoundException.class,() -> organizationClient.updateOrganization(organizationDetailDTO, accessToken));
  }

  @Test
  void givenBadRequestWhenUpdateOrganizationThenInvalidOrganizationException() {
    OrganizationDetailDTO organizationDetailDTO = new OrganizationDetailDTO();
    String accessToken = "ACCESSTOKEN";
    OrganizationErrorDTO errorDTO = TestUtils.getPodamFactory().manufacturePojo(OrganizationErrorDTO.class);

    String body = """
    {
      "code": "INVALID_ORGANIZATION",
      "message": "%s",
      "traceId": "t1"
    }
    """.formatted(errorDTO.getMessage());

    HttpClientErrorException badRequest =
      HttpClientErrorException.create(
        HttpStatus.BAD_REQUEST,
        "Bad Request",
        HttpHeaders.EMPTY,
        body.getBytes(StandardCharsets.UTF_8),
        StandardCharsets.UTF_8
      );

    Mockito.when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);

    Mockito.doThrow(badRequest)
      .when(organizationApiMock)
      .updateOrganization(organizationDetailDTO);

    InvalidOrganizationException e =
      Assertions.assertThrows(
        InvalidOrganizationException.class,
        () -> organizationClient.updateOrganization(organizationDetailDTO, accessToken)
      );

    Assertions.assertNotNull(e);
    Assertions.assertEquals("INVALID_ORGANIZATION", e.getCode());
    Assertions.assertEquals(errorDTO.getMessage(), e.getMessage());
  }
}
