package it.gov.pagopa.pu.bff.connector.registries.config;

import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.registries.dto.generated.RegistryOutcome;
import it.gov.pagopa.pu.registries.dto.generated.RegistryPagoPaEventType;
import it.gov.pagopa.pu.registries.dto.generated.RegistrySilEventType;
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

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistriesApisHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private RegistriesApisHolder apisHolder;
  private RegistriesApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = RegistriesApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new RegistriesApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getDebtPositionRegistrySearchControllerApi(null));
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
      accessToken -> apisHolder.getDebtPositionRegistrySearchControllerApi(accessToken)
        .crudDebtPositionRegistriesFindAllByDebtPositionId(null),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetDebtPositionRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionRegistrySearchControllerApi(accessToken)
        .crudDebtPositionRegistriesFindAllByDebtPositionId(null),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetInstallmentRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getInstallmentRegistrySearchControllerApi(accessToken)
        .crudInstallmentRegistriesFindAllByDebtPositionId(null),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetPagoPaRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getPagoPaRegistrySearchControllerApi(accessToken)
        .crudPagopaRegistriesSearchByFilters(RegistryPagoPaEventType.GPD_createPosition, OffsetDateTime.now(), OffsetDateTime.now(), "orgFiscalCode", "iuv", RegistryOutcome.OK, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetSilRegistryApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getSilRegistryApi(accessToken)
        .getSilRegistry("666"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetPagoPaRegistryApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getPagoPaRegistryApi(accessToken)
        .getPagoPaRegistry("pagoPaRegistryId"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetSilRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getSilRegistrySearchControllerApi(accessToken)
        .crudSilRegistriesSearchByFilters(RegistrySilEventType.PTPR_pivotSILAutorizzaImportFlusso, OffsetDateTime.now(), OffsetDateTime.now(), "orgFiscalCode", "iuv", RegistryOutcome.OK, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }
}
