package it.gov.pagopa.pu.bff.connector.organization.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.p4pa_organization.controller.ApiClient;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Broker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ExtendWith(MockitoExtension.class)
class OrganizationApiHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private OrganizationApisHolder organizationApisHolder;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    ApiClient apiClient = new ApiClient(restTemplateMock);
    String baseUrl = "http://example.com";
    apiClient.setBasePath(baseUrl);
    organizationApisHolder = new OrganizationApisHolder(baseUrl, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock
    );
  }

  @Test
  void whenGetOrganizationSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getOrganizationSearchControllerApi(accessToken)
        .crudOrganizationsFindByIpaCode("IPACODE"),
      Organization.class,
      organizationApisHolder::unload);
  }

  @Test
  void whenGetAuthnApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getBrokerEntityControllerApi(accessToken)
        .crudGetBroker("BROKERID"),
      Broker.class,
      organizationApisHolder::unload);
  }

  @Test
  void whenGetTaxonomyEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getTaxonomyEntityControllerApi(accessToken)
        .crudTaxonomiesCollectionReasonFindCollectionReasons(null,null,null),
      CollectionModelTaxonomyCollectionReasonDTO.class,
      organizationApisHolder::unload);
  }

  @Test
  void whenGetMacroAreaThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getMacroArea(accessToken)
        .crudTaxonomiesMacroAreaFindMacroAreaCodes(null),
      CollectionModelTaxonomyMacroAreaCodeDTO.class,
      organizationApisHolder::unload);
  }

  @Test
  void whenGetOrganizationTypesThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getOrganizationTypes(accessToken)
        .crudTaxonomiesOrganizationTypesFindOrganizationTypes(),
      CollectionModelTaxonomyOrganizationTypeDTO.class,
      organizationApisHolder::unload);
  }

  @Test
  void whenGetServiceTypeThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getServiceType(accessToken)
        .crudTaxonomiesServiceTypeFindServiceTypeCodes(null,null),
      CollectionModelTaxonomyServiceTypeCodeDTO.class,
      organizationApisHolder::unload);
  }

  @Test
  void whenGetTaxonomyCodeThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> organizationApisHolder.getTaxonomyCode(accessToken)
        .crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null,null,null,null),
      CollectionModelTaxonomyCodeDTO.class,
      organizationApisHolder::unload);
  }

}
