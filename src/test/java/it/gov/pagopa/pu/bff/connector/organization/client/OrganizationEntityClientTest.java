package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.OrganizationEntityControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationEntityClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private OrganizationEntityControllerApi organizationEntityControllerApiMock;

  private OrganizationEntityClient organizationEntityClient;

  @BeforeEach
  void setUp() {
    organizationEntityClient = new OrganizationEntityClient(organizationApisHolder);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder,
      organizationEntityControllerApiMock
    );
  }

  @Test
  void whenGetOrganizationByIpaCodeThenInvokeWithAccessToken() {
    // Given
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    Organization expectedResult = new Organization();

    when(organizationApisHolder.getOrganizationEntityControllerApi(accessToken))
      .thenReturn(organizationEntityControllerApiMock);
    when(organizationEntityControllerApiMock.crudGetOrganization(String.valueOf(organizationId)))
      .thenReturn(expectedResult);

    // When
    Organization result = organizationEntityClient.getOrganizationByOrganizationId(organizationId, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentIpaCodeWhenGetOrganizationByIpaCodeThenNull() {
    // Given
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolder.getOrganizationEntityControllerApi(accessToken))
      .thenReturn(organizationEntityControllerApiMock);
    when(organizationEntityControllerApiMock.crudGetOrganization(String.valueOf(organizationId)))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    // When
    Organization result = organizationEntityClient.getOrganizationByOrganizationId(organizationId, accessToken);

    // Then
    Assertions.assertNull(result);
  }
}
