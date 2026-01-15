package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.UpstreamErrorMapper;
import it.gov.pagopa.pu.organization.controller.generated.OrganizationApi;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
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
  private UpstreamErrorMapper upstreamErrorMapperMock;

  private OrganizationClient organizationClient;

  @BeforeEach
  void setUp() {
    organizationClient = new OrganizationClient(organizationApisHolderMock, upstreamErrorMapperMock);
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

    String upstreamMessage = "[INVALID_ORGANIZATION] Error from upstream";
    String body = """
    {"code":"UPSTREAM_CODE","message":"%s","traceId":"t1"}
    """.formatted(upstreamMessage);

    HttpClientErrorException badRequest = HttpClientErrorException.create(
      HttpStatus.BAD_REQUEST,
      "Bad Request",
      HttpHeaders.EMPTY,
      body.getBytes(StandardCharsets.UTF_8),
      StandardCharsets.UTF_8
    );

    Mockito.when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    Mockito.doThrow(badRequest)
      .when(organizationApiMock).updateOrganization(organizationDetailDTO);

    InvalidOrganizationException ex = Assertions.assertThrows(
      InvalidOrganizationException.class,
      () -> organizationClient.updateOrganization(organizationDetailDTO, accessToken)
    );

    Assertions.assertEquals("INVALID_ORGANIZATION", ex.getCode());
    Assertions.assertEquals("Error from upstream", ex.getMessage());
  }
}
