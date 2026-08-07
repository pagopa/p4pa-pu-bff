package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.organization.controller.generated.OrgSubUnitEntityControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnit;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitRequestBody;
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

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrgSubUnitEntityClientTest {

  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private OrgSubUnitEntityControllerApi orgSubUnitEntityControllerApiMock;

  private OrgSubUnitEntityClient orgSubUnitEntityClient;

  @BeforeEach
  void setUp() {
    orgSubUnitEntityClient = new OrgSubUnitEntityClient(organizationApisHolder);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder,
      orgSubUnitEntityControllerApiMock
    );
  }

  @Test
  void whenGetOrgSubUnitByIdThenInvokeWithAccessToken() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    OrgSubUnit expectedResult = new OrgSubUnit();

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    when(orgSubUnitEntityControllerApiMock.crudGetOrgsubunit(orgSubUnitId))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitEntityClient.getOrgSubUnitById(orgSubUnitId, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentIdWhenGetOrgSubUnitByIdThenNull() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    when(orgSubUnitEntityControllerApiMock.crudGetOrgsubunit(orgSubUnitId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When
    OrgSubUnit result = orgSubUnitEntityClient.getOrgSubUnitById(orgSubUnitId, accessToken);

    // Then
    Assertions.assertNull(result);
  }

  @Test
  void whenCreateOrgSubUnitThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    OrgSubUnitRequestBody requestBody = new OrgSubUnitRequestBody();
    OrgSubUnit expectedResult = new OrgSubUnit();

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    when(orgSubUnitEntityControllerApiMock.crudCreateOrgsubunit(requestBody))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitEntityClient.createOrgSubUnit(requestBody, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenDeleteOrgSubUnitThenInvokeWithAccessToken() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);

    // When & Then
    Assertions.assertDoesNotThrow(() -> orgSubUnitEntityClient.deleteOrgSubUnit(orgSubUnitId, accessToken));
    verify(orgSubUnitEntityControllerApiMock).crudDeleteOrgsubunit(orgSubUnitId);
  }

  @Test
  void givenNoExistentIdWhenDeleteOrgSubUnitThenThrowResourceNotFoundException() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);

    doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null))
      .when(orgSubUnitEntityControllerApiMock).crudDeleteOrgsubunit(orgSubUnitId);

    // When & Then
    NotFoundException exception = Assertions.assertThrows(
      NotFoundException.class,
      () -> orgSubUnitEntityClient.deleteOrgSubUnit(orgSubUnitId, accessToken)
    );
    Assertions.assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
  }

  @Test
  void whenUpdateOrgSubUnitThenInvokeWithAccessToken() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    OrgSubUnitRequestBody requestBody = new OrgSubUnitRequestBody();
    OrgSubUnit expectedResult = new OrgSubUnit();

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    when(orgSubUnitEntityControllerApiMock.crudUpdateOrgsubunit(orgSubUnitId, requestBody))
      .thenReturn(expectedResult);

    // When
    OrgSubUnit result = orgSubUnitEntityClient.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNoExistentIdWhenUpdateOrgSubUnitThenThrowResourceNotFoundException() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    OrgSubUnitRequestBody requestBody = new OrgSubUnitRequestBody();

    when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    when(orgSubUnitEntityControllerApiMock.crudUpdateOrgsubunit(orgSubUnitId, requestBody))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When & Then
    NotFoundException exception = Assertions.assertThrows(
      NotFoundException.class,
      () -> orgSubUnitEntityClient.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken)
    );
    Assertions.assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
  }

}
