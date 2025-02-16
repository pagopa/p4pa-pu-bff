package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.controller.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    Mockito.when(taxonomyCollectionReasonDtoSearchControllerApiMock.crudTaxonomiesCollectionReasonFindCollectionReasons(null, null, null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyCollectionReasonDTO result = taxonomyClient.getCollectionReason(null, null, null, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
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
  void whenGetServiceTypeThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyServiceTypeCodeDTO expectedResult = new CollectionModelTaxonomyServiceTypeCodeDTO();

    Mockito.when(organizationApisHolder.getServiceType(accessToken))
      .thenReturn(taxonomyServiceTypeCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyServiceTypeCodeDtoSearchControllerApiMock.crudTaxonomiesServiceTypeFindServiceTypeCodes(null, null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyServiceTypeCodeDTO result = taxonomyClient.getServiceType(null, null, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenGetTaxonomyCodeThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyCodeDTO expectedResult = new CollectionModelTaxonomyCodeDTO();

    Mockito.when(organizationApisHolder.getTaxonomyCode(accessToken))
      .thenReturn(taxonomyCodeDtoSearchControllerApiMock);
    Mockito.when(taxonomyCodeDtoSearchControllerApiMock.crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null, null, null, null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyCodeDTO result = taxonomyClient.getTaxonomyCode(null, null, null, null, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

}
