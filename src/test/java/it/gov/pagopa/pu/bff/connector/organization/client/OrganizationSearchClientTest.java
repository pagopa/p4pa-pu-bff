package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.EntityModelOrganization;
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
class OrganizationSearchClientTest {
  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private OrganizationSearchControllerApi organizationSearchControllerApiMock;

  private OrganizationSearchClient organizationSearchClient;

  @BeforeEach
  void setUp() {
    organizationSearchClient = new OrganizationSearchClient(organizationApisHolder);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder
    );
  }

  @Test
  void whenGetOrganizationByIpaCodeThenInvokeWithAccessToken() {
    // Given
    String orgIpaCode = "ORGIPACODE";
    String accessToken = "ACCESSTOKEN";
    EntityModelOrganization expectedResult = new EntityModelOrganization();

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.executeSearchOrganizationGet(orgIpaCode))
      .thenReturn(expectedResult);

    // When
    EntityModelOrganization result = organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentIpaCodeWhenGetOrganizationByIpaCodeThenNull() {
    // Given
    String orgIpaCode = "ORGIPACODE";
    String accessToken = "ACCESSTOKEN";

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.executeSearchOrganizationGet(orgIpaCode))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    // When
    EntityModelOrganization result = organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken);

    // Then
    Assertions.assertNull(result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetOrganizationByIpaCodeThenThrowIt() {
    // Given
    String orgIpaCode = "ORGIPACODE";
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.executeSearchOrganizationGet(orgIpaCode))
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetOrganizationByIpaCodeThenThrowIt() {
    // Given
    String orgIpaCode = "ORGIPACODE";
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.executeSearchOrganizationGet(orgIpaCode))
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> organizationSearchClient.getOrganizationByIpaCode(orgIpaCode, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }
}
