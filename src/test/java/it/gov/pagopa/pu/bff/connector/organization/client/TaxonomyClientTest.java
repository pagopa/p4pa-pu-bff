package it.gov.pagopa.pu.bff.connector.organization.client;

import it.gov.pagopa.pu.bff.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.bff.connector.workflow_hub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.*;
import it.gov.pagopa.pu.organization.dto.generated.*;
import it.gov.pagopa.pu.workflowhub.client.generated.TaxonomyApi;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxonomyClientTest {

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private WorkflowHubApisHolder workflowHubApisHolderMock;
  @Mock
  private TaxonomySearchControllerApi taxonomySearchControllerApiMock;
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
  @Mock
  private TaxonomyEntityControllerApi taxonomyEntityControllerApiMock;
  @Mock
  private TaxonomyApi taxonomyApiMock;

  private TaxonomyClient taxonomyClient;

  @BeforeEach
  void setUp() {
    taxonomyClient = new TaxonomyClient(organizationApisHolderMock, workflowHubApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      workflowHubApisHolderMock,
      taxonomySearchControllerApiMock,
      taxonomyCollectionReasonDtoSearchControllerApiMock,
      taxonomyMacroAreaCodeDtoSearchControllerApiMock,
      taxonomyOrganizationTypeDtoSearchControllerApiMock,
      taxonomyServiceTypeCodeDtoSearchControllerApiMock,
      taxonomyCodeDtoSearchControllerApiMock,
      taxonomyEntityControllerApiMock
    );
  }

  @Test
  void whenGetTaxonomyDetailThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long taxonomyId = 123L;
    Taxonomy expectedResult = new Taxonomy();

    when(organizationApisHolderMock.getTaxonomy(accessToken))
      .thenReturn(taxonomyEntityControllerApiMock);
    when(taxonomyEntityControllerApiMock.crudGetTaxonomy(String.valueOf(taxonomyId)))
      .thenReturn(expectedResult);

    Taxonomy result = taxonomyClient.getTaxonomyDetail(taxonomyId, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenTaxonomyNotFoundThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    Long taxonomyId = 123L;

    when(organizationApisHolderMock.getTaxonomy(accessToken))
      .thenReturn(taxonomyEntityControllerApiMock);
    when(taxonomyEntityControllerApiMock.crudGetTaxonomy(String.valueOf(taxonomyId)))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    Taxonomy result = taxonomyClient.getTaxonomyDetail(taxonomyId, accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoMoreInteractions(taxonomyEntityControllerApiMock, organizationApisHolderMock);
  }

  @Test
  void whenGetByTaxonomyCodeThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    Taxonomy expectedResult = new Taxonomy();

    when(organizationApisHolderMock.getTaxonomySearchControllerApi(accessToken))
      .thenReturn(taxonomySearchControllerApiMock);
    when(taxonomySearchControllerApiMock.crudTaxonomiesFindByTaxonomyCode("TAX"))
      .thenReturn(expectedResult);

    // When
    Taxonomy result = taxonomyClient.getTaxonomyByTaxonomyCode("TAX", accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenGetCollectionReasonThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    CollectionModelTaxonomyCollectionReasonDTO expectedResult = new CollectionModelTaxonomyCollectionReasonDTO();

    when(organizationApisHolderMock.getTaxonomyEntityControllerApi(accessToken))
      .thenReturn(taxonomyCollectionReasonDtoSearchControllerApiMock);
    when(taxonomyCollectionReasonDtoSearchControllerApiMock.crudTaxonomiesCollectionReasonFindCollectionReasons(null, null, null))
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

    when(organizationApisHolderMock.getMacroArea(accessToken))
      .thenReturn(taxonomyMacroAreaCodeDtoSearchControllerApiMock);
    when(taxonomyMacroAreaCodeDtoSearchControllerApiMock.crudTaxonomiesMacroAreaFindMacroAreaCodes(null))
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

    when(organizationApisHolderMock.getOrganizationTypes(accessToken))
      .thenReturn(taxonomyOrganizationTypeDtoSearchControllerApiMock);
    when(taxonomyOrganizationTypeDtoSearchControllerApiMock.crudTaxonomiesOrganizationTypesFindOrganizationTypes())
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

    when(organizationApisHolderMock.getServiceType(accessToken))
      .thenReturn(taxonomyServiceTypeCodeDtoSearchControllerApiMock);
    when(taxonomyServiceTypeCodeDtoSearchControllerApiMock.crudTaxonomiesServiceTypeFindServiceTypeCodes(null, null))
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

    when(organizationApisHolderMock.getTaxonomyCode(accessToken))
      .thenReturn(taxonomyCodeDtoSearchControllerApiMock);
    when(taxonomyCodeDtoSearchControllerApiMock.crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null, null, null, null))
      .thenReturn(expectedResult);

    // When
    CollectionModelTaxonomyCodeDTO result = taxonomyClient.getTaxonomyCode(null, null, null, null, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenGetTaxonomiesThenInvokeWithAccessToken() {
    // Given
    String accessToken = "ACCESSTOKEN";
    PagedModelTaxonomy expectedResult = new PagedModelTaxonomy();

    when(
        organizationApisHolderMock.getTaxonomySearchControllerApi(accessToken))
      .thenReturn(taxonomySearchControllerApiMock);
    when(taxonomySearchControllerApiMock.crudTaxonomiesFindTaxonomies("organizationType", "macroAreaCode", "serviceTypeCode",
        "collectionReason", 0, 10, Collections.emptyList()))
      .thenReturn(expectedResult);

    // When
    PagedModelTaxonomy result = taxonomyClient.getTaxonomies("organizationType", "macroAreaCode", "serviceTypeCode",
      "collectionReason", PageRequest.of(0,10, Sort.unsorted()), accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenSynchronizeTaxonomyThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    WorkflowCreatedDTO expectedResult = new WorkflowCreatedDTO();

    when(workflowHubApisHolderMock.getTaxonomyApi(accessToken))
      .thenReturn(taxonomyApiMock);
    when(taxonomyApiMock.synchronizeTaxonomy())
      .thenReturn(expectedResult);

    WorkflowCreatedDTO result = taxonomyClient.synchronizeTaxonomy(accessToken);

    Assertions.assertSame(expectedResult, result);
  }

}
