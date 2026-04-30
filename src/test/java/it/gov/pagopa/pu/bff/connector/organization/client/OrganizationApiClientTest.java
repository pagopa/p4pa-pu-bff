package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class OrganizationApiClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrganizationApi organizationApiMock;

  private OrganizationApiClient organizationApiClient;

  @BeforeEach
  void setUp() {
    organizationApiClient = new OrganizationApiClient(organizationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      organizationApiMock
    );
  }

  @Test
  void whenGetOrganizationDetailThenInvokeWithAccessToken() {
    Long organizationId = 123L;
    String accessToken = "ACCESSTOKEN";
    OrganizationDetailDTO expectedResult = new OrganizationDetailDTO();

    Mockito.when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    Mockito.when(organizationApiMock.getOrganization(organizationId))
      .thenReturn(expectedResult);

    OrganizationDetailDTO result = organizationApiClient.getOrganizationDetail(organizationId, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNonExistingOrganizationIdWhenGetOrganizationDetailThenNull() {
    Long organizationId = 123L;
    String accessToken = "ACCESSTOKEN";

    Mockito.when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    Mockito.when(organizationApiMock.getOrganization(organizationId))
      .thenThrow(HttpClientErrorException.create(
        HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    OrganizationDetailDTO result = organizationApiClient.getOrganizationDetail(organizationId, accessToken);

    Assertions.assertNull(result);
  }
}
