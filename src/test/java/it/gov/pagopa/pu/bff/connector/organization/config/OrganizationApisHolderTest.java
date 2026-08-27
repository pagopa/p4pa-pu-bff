package it.gov.pagopa.pu.bff.connector.organization.config;

import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.organization.dto.generated.OrgSilServiceType;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import it.gov.pagopa.pu.organization.dto.generated.PdndServiceRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationApisHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private OrganizationApisHolder apisHolder;
  private OrganizationApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = OrganizationApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new OrganizationApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getOrganizationSearchControllerApi(null));
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock
    );
  }

  @Test
  void testRetryConfiguration() {
    assertRetry(apiClientConfig,
      accessToken -> apisHolder.getOrganizationSearchControllerApi(accessToken)
        .crudOrganizationsFindByIpaCode("IPACODE"),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetOrganizationSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrganizationSearchControllerApi(accessToken)
        .crudOrganizationsFindByIpaCode("IPACODE"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetAuthnApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getBrokerEntityControllerApi(accessToken)
        .crudGetBroker("BROKERID"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetTaxonomyThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTaxonomy(accessToken)
        .crudGetTaxonomy("123"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload
    );
  }

  @Test
  void whenGetTaxonomySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTaxonomySearchControllerApi(accessToken)
        .crudTaxonomiesFindByTaxonomyCode(null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetTaxonomyEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTaxonomyEntityControllerApi(accessToken)
        .crudTaxonomiesCollectionReasonFindCollectionReasons(null,null,null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetMacroAreaThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getMacroArea(accessToken)
        .crudTaxonomiesMacroAreaFindMacroAreaCodes(null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrganizationTypesThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrganizationTypes(accessToken)
        .crudTaxonomiesOrganizationTypesFindOrganizationTypes(),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetServiceTypeThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getServiceType(accessToken)
        .crudTaxonomiesServiceTypeFindServiceTypeCodes(null,null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetTaxonomyCodeThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTaxonomyCode(accessToken)
        .crudTaxonomiesTaxonomyCodeFindTaxonomyCodes(null,null,null,null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrganizationEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrganizationEntityControllerApi(accessToken)
        .crudGetOrganization("1"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrgSilServiceSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrgSilServiceSearchControllerApi(accessToken)
        .crudOrgSilServicesFindAllByOrganizationIdAndServiceType(1L, OrgSilServiceType.ACTUALIZATION),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrgSilServiceEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrgSilServiceEntityControllerApi(accessToken)
        .crudGetOrgsilservice(String.valueOf(1L)),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrgSilServiceViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrgSilServiceViewSearchControllerApi(accessToken)
        .crudOrgSilServicesViewFindOrgSilServicesByFilters(
          1L,"appName",OrgSilServiceType.ACTUALIZATION,true,0,0,Collections.emptyList()),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrganizationSilServiceApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrganizationSilServiceApi(accessToken)
        .getOrgSilService(1L),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetOrganizationApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken ->{
        apisHolder.getOrganizationApi(accessToken)
                .updateOrganization(new OrganizationDetailDTO());
        return voidMock;
      },
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetBrokerConfigurationEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getBrokerConfigurationEntityControllerApi(accessToken)
        .crudGetBrokerconfiguration("1"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenOrgSubUnitEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getOrgSubUnitEntityControllerApi(accessToken)
        .crudGetOrgsubunit("ID"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetPdndClientApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getPdndClientApi(accessToken)
        .getPdndClientsByOrganizationIdAndSubUnitCode(1L, "subUnitCode"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetPdndServiceApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getPdndServiceApi(accessToken)
        .savePdndService(1L, new PdndServiceRequestDTO(), "subUnitCode"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }
}
