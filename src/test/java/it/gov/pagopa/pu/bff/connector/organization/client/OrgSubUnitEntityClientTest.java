package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.exception.InvalidOrganizationException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.UpstreamErrorMapper;
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


@ExtendWith(MockitoExtension.class)
class OrgSubUnitEntityClientTest {

  @Mock
  private OrganizationApisHolder organizationApisHolder;
  @Mock
  private UpstreamErrorMapper upstreamErrorMapper;
  @Mock
  private OrgSubUnitEntityControllerApi orgSubUnitEntityControllerApiMock;

  private OrgSubUnitEntityClient orgSubUnitEntityClient;

  @BeforeEach
  void setUp() {
    orgSubUnitEntityClient = new OrgSubUnitEntityClient(organizationApisHolder, upstreamErrorMapper);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder,
      upstreamErrorMapper,
      orgSubUnitEntityControllerApiMock
    );
  }

  @Test
  void whenGetOrgSubUnitByIdThenInvokeWithAccessToken() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    OrgSubUnit expectedResult = new OrgSubUnit();

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    Mockito.when(orgSubUnitEntityControllerApiMock.crudGetOrgsubunit(orgSubUnitId))
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

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    Mockito.when(orgSubUnitEntityControllerApiMock.crudGetOrgsubunit(orgSubUnitId))
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

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    Mockito.when(orgSubUnitEntityControllerApiMock.crudCreateOrgsubunit(requestBody))
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

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);

    // When & Then
    Assertions.assertDoesNotThrow(() -> orgSubUnitEntityClient.deleteOrgSubUnit(orgSubUnitId, accessToken));
    Mockito.verify(orgSubUnitEntityControllerApiMock).crudDeleteOrgsubunit(orgSubUnitId);
  }

  @Test
  void givenNoExistentIdWhenDeleteOrgSubUnitThenThrowResourceNotFoundException() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);

    Mockito.doThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null))
      .when(orgSubUnitEntityControllerApiMock).crudDeleteOrgsubunit(orgSubUnitId);

    // When & Then
    ResourceNotFoundException exception = Assertions.assertThrows(
      ResourceNotFoundException.class,
      () -> orgSubUnitEntityClient.deleteOrgSubUnit(orgSubUnitId, accessToken)
    );
    Assertions.assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
  }

  @Test
  void givenBadRequestWhenDeleteOrgSubUnitThenThrowInvalidOrganizationException() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException.BadRequest badRequestException = (HttpClientErrorException.BadRequest) HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "BadRequest", null, null, null);
    UpstreamErrorMapper.MappedUpstreamError mappedError = new UpstreamErrorMapper.MappedUpstreamError("ERR_CODE", "Err Description");

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);

    Mockito.doThrow(badRequestException)
      .when(orgSubUnitEntityControllerApiMock).crudDeleteOrgsubunit(orgSubUnitId);

    Mockito.when(upstreamErrorMapper.from(badRequestException))
      .thenReturn(mappedError);

    // When & Then
    InvalidOrganizationException exception = Assertions.assertThrows(
      InvalidOrganizationException.class,
      () -> orgSubUnitEntityClient.deleteOrgSubUnit(orgSubUnitId, accessToken)
    );
    Assertions.assertEquals("ERR_CODE", exception.getCode());
  }

  @Test
  void whenUpdateOrgSubUnitThenInvokeWithAccessToken() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    OrgSubUnitRequestBody requestBody = new OrgSubUnitRequestBody();
    OrgSubUnit expectedResult = new OrgSubUnit();

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    Mockito.when(orgSubUnitEntityControllerApiMock.crudUpdateOrgsubunit(orgSubUnitId, requestBody))
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

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    Mockito.when(orgSubUnitEntityControllerApiMock.crudUpdateOrgsubunit(orgSubUnitId, requestBody))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When & Then
    ResourceNotFoundException exception = Assertions.assertThrows(
      ResourceNotFoundException.class,
      () -> orgSubUnitEntityClient.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken)
    );
    Assertions.assertEquals("ORG_SUB_UNIT_NOT_FOUND", exception.getCode());
  }

  @Test
  void givenBadRequestWhenUpdateOrgSubUnitThenThrowInvalidOrganizationException() {
    // Given
    String orgSubUnitId = "SUB_UNIT_ID";
    String accessToken = "ACCESSTOKEN";
    OrgSubUnitRequestBody requestBody = new OrgSubUnitRequestBody();
    HttpClientErrorException.BadRequest badRequestException = (HttpClientErrorException.BadRequest) HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "BadRequest", null, null, null);
    UpstreamErrorMapper.MappedUpstreamError mappedError = new UpstreamErrorMapper.MappedUpstreamError("ERR_CODE", "Err Description");

    Mockito.when(organizationApisHolder.getOrgSubUnitEntityControllerApi(accessToken))
      .thenReturn(orgSubUnitEntityControllerApiMock);
    Mockito.when(orgSubUnitEntityControllerApiMock.crudUpdateOrgsubunit(orgSubUnitId, requestBody))
      .thenThrow(badRequestException);
    Mockito.when(upstreamErrorMapper.from(badRequestException))
      .thenReturn(mappedError);

    // When & Then
    InvalidOrganizationException exception = Assertions.assertThrows(
      InvalidOrganizationException.class,
      () -> orgSubUnitEntityClient.updateOrgSubUnit(orgSubUnitId, requestBody, accessToken)
    );
    Assertions.assertEquals("ERR_CODE", exception.getCode());
  }

}
