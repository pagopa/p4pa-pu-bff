package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyCodeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyCollectionReasonDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyMacroAreaCodeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyOrganizationTypeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.controller.generated.TaxonomyServiceTypeCodeDtoSearchControllerApi;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
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
class TaxonomyClientTest {

  @Mock
  private OrganizationApisHolder organizationApisHolder;

  @Mock
  private TaxonomyCollectionReasonDtoSearchControllerApi taxonomyCollectionReasonDtoSearchControllerApiMock;

  @Mock
  private TaxonomyMacroAreaCodeDtoSearchControllerApi taxonomyMacroAreaCodeDtoSearchControllerApiMock;
  @Mock
  private TaxonomyOrganizationTypeDtoSearchControllerApi taxonomyOrganizationTypeDtoSearchControllerApiMock;

  @Mock
  private TaxonomyServiceTypeCodeDtoSearchControllerApi taxonomyServiceTypeCodeDtoSearchControllerApiMock;

  @Mock
  private TaxonomyCodeDtoSearchControllerApi taxonomyCodeDtoSearchControllerApiMock;
  private TaxonomyClient taxonomyClient;

  @BeforeEach
  void setUp() {
    taxonomyClient = new TaxonomyClient(organizationApisHolder);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolder
    );
  }

  @Test
  void whenGetCollectionReasonThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyCollectionReasonDTO expectedResult = new CollectionModelTaxonomyCollectionReasonDTO();

    Mockito.when(organizationApisHolder.getTaxonomyEntityControllerApi(accessToken))
      .thenReturn(taxonomyCollectionReasonDtoSearchControllerApiMock);
    Mockito.when(taxonomyCollectionReasonDtoSearchControllerApiMock.crudTaxonomiesCollectionReasonFindCollectionReasons(null,null,null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyCollectionReasonDTO result = taxonomyClient.getCollectionReason(null,null,null, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetCollectionReasonThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getTaxonomyEntityControllerApi(accessToken))
      .thenReturn(taxonomyCollectionReasonDtoSearchControllerApiMock);
    Mockito.when(taxonomyCollectionReasonDtoSearchControllerApiMock.crudTaxonomiesCollectionReasonFindCollectionReasons(null,null,null))
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getCollectionReason(null,null,null, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetCollectionReasonThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getTaxonomyEntityControllerApi(accessToken))
      .thenReturn(taxonomyCollectionReasonDtoSearchControllerApiMock);
    Mockito.when(taxonomyCollectionReasonDtoSearchControllerApiMock.crudTaxonomiesCollectionReasonFindCollectionReasons(null,null,null))
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getCollectionReason(null,null,null, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void whenGetMacroAreaThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyMacroAreaCodeDTO expectedResult = new CollectionModelTaxonomyMacroAreaCodeDTO();

    Mockito.when(organizationApisHolder.getMacroArea(accessToken))
      .thenReturn(taxonomyMacroAreaCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyMacroAreaCodeDtoSearchControllerApiMock.crudTaxonomiesMacroAreaFindMacroAreaCodes(null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyMacroAreaCodeDTO result = taxonomyClient.getMacroArea(null, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetMacroAreaThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getMacroArea(accessToken))
      .thenReturn(taxonomyMacroAreaCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyMacroAreaCodeDtoSearchControllerApiMock.crudTaxonomiesMacroAreaFindMacroAreaCodes(null))
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getMacroArea(null, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetMacroAreaThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getMacroArea(accessToken))
      .thenReturn(taxonomyMacroAreaCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyMacroAreaCodeDtoSearchControllerApiMock.crudTaxonomiesMacroAreaFindMacroAreaCodes(null))
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getMacroArea(null, accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }
  @Test
  void whenGetOrganizationTypeThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyOrganizationTypeDTO expectedResult = new CollectionModelTaxonomyOrganizationTypeDTO();

    Mockito.when(organizationApisHolder.getOrganizationTypes(accessToken))
      .thenReturn(taxonomyOrganizationTypeDtoSearchControllerApiMock);
    Mockito.when(taxonomyOrganizationTypeDtoSearchControllerApiMock.crudTaxonomiesOrganizationTypesFindOrganizationTypes())
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyOrganizationTypeDTO result = taxonomyClient.getOrganizationType(accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetOrganizationThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getOrganizationTypes(accessToken))
      .thenReturn(taxonomyOrganizationTypeDtoSearchControllerApiMock);
    Mockito.when(taxonomyOrganizationTypeDtoSearchControllerApiMock.crudTaxonomiesOrganizationTypesFindOrganizationTypes())
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getOrganizationType(accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetOrganizationThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getOrganizationTypes(accessToken))
      .thenReturn(taxonomyOrganizationTypeDtoSearchControllerApiMock);
    Mockito.when(taxonomyOrganizationTypeDtoSearchControllerApiMock.crudTaxonomiesOrganizationTypesFindOrganizationTypes())
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getOrganizationType(accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }
  @Test
  void whenGetServiceTypeThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyServiceTypeCodeDTO expectedResult = new CollectionModelTaxonomyServiceTypeCodeDTO();

    Mockito.when(organizationApisHolder.getServiceType(accessToken))
      .thenReturn(taxonomyServiceTypeCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyServiceTypeCodeDtoSearchControllerApiMock.crudTaxonomiesServiceTypeFindServiceTypeCodes(null,null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyServiceTypeCodeDTO result = taxonomyClient.getServiceType(null,null,accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetServiceTypeThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getServiceType(accessToken))
      .thenReturn(taxonomyServiceTypeCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyServiceTypeCodeDtoSearchControllerApiMock.crudTaxonomiesServiceTypeFindServiceTypeCodes(null,null))
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getServiceType(null,null,accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetServiceTypeThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getServiceType(accessToken))
      .thenReturn(taxonomyServiceTypeCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyServiceTypeCodeDtoSearchControllerApiMock.crudTaxonomiesServiceTypeFindServiceTypeCodes(null,null))
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getServiceType(null,null,accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }
  @Test
  void whenGetTaxonomyCodeThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyCodeDTO expectedResult = new CollectionModelTaxonomyCodeDTO();

    Mockito.when(organizationApisHolder.getTaxonomyCode(accessToken))
      .thenReturn(taxonomyCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyCodeDtoSearchControllerApiMock.crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null,null,null,null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyCodeDTO result = taxonomyClient.getTaxonomyCode(null,null,null,null,accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetTaxonomyCodeThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    Mockito.when(organizationApisHolder.getTaxonomyCode(accessToken))
      .thenReturn(taxonomyCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyCodeDtoSearchControllerApiMock.crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null,null,null,null))
      .thenThrow(expectedException);

    // When
    HttpClientErrorException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getTaxonomyCode(null,null,null,null,accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetTaxonomyCodeThenThrowIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    Mockito.when(organizationApisHolder.getTaxonomyCode(accessToken))
      .thenReturn(taxonomyCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyCodeDtoSearchControllerApiMock.crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null,null,null,null))
      .thenThrow(expectedException);

    // When
    RuntimeException result = Assertions.assertThrows(expectedException.getClass(), () -> taxonomyClient.getTaxonomyCode(null,null,null,null,accessToken));

    // Then
    Assertions.assertSame(expectedException, result);
  }

}
