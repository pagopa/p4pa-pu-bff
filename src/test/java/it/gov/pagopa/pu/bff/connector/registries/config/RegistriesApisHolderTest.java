package it.gov.pagopa.pu.bff.connector.registries.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.registries.dto.generated.RegistryPagoPaEventType;
import it.gov.pagopa.pu.registries.dto.generated.RegistrySilEventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.OffsetDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class RegistriesApisHolderTest  extends BaseApiHolderTest {
    @Mock
    private RestTemplateBuilder restTemplateBuilderMock;

    private RegistriesApisHolder registriesApisHolder;

    @BeforeEach
    void setUp() {
        Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
        Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
        RegistriesApiClientConfig clientConfig = RegistriesApiClientConfig.builder()
                .baseUrl("http://example.com")
                .build();
        registriesApisHolder = new RegistriesApisHolder(clientConfig, restTemplateBuilderMock);
    }

    @AfterEach
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(
            restTemplateBuilderMock,
            restTemplateMock
        );
    }

    @Test
    void whenGetDebtPositionRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
        assertAuthenticationShouldBeSetInThreadSafeMode(
            accessToken -> registriesApisHolder.getDebtPositionRegistrySearchControllerApi(accessToken)
                .crudDebtPositionRegistriesFindAllByDebtPositionId(null),
            new ParameterizedTypeReference<>() {},
            registriesApisHolder::unload);
    }

  @Test
  void whenGetInstallmentRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> registriesApisHolder.getInstallmentRegistrySearchControllerApi(accessToken)
        .crudInstallmentRegistriesFindAllByDebtPositionId(null),
      new ParameterizedTypeReference<>() {},
      registriesApisHolder::unload);
  }

  @Test
  void whenGetPagoPaRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> registriesApisHolder.getPagoPaRegistrySearchControllerApi(accessToken)
        .crudPagopaRegistriesSearchByFilters(RegistryPagoPaEventType.createPosition, OffsetDateTime.now(),OffsetDateTime.now(),"orgFiscalCode","iuv",0,10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {},
      registriesApisHolder::unload);
  }

  @Test
  void whenGetSilRegistryApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> registriesApisHolder.getSilRegistryApi(accessToken)
        .getSilRegistry("666"),
      new ParameterizedTypeReference<>() {},
      registriesApisHolder::unload);
  }

  @Test
  void whenGetPagoPaRegistryApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> registriesApisHolder.getPagoPaRegistryApi(accessToken)
        .getPagoPaRegistry("pagoPaRegistryId"),
      new ParameterizedTypeReference<>() {},
      registriesApisHolder::unload);
  }

  @Test
  void whenGetSilRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> registriesApisHolder.getSilRegistrySearchControllerApi(accessToken)
        .crudSilRegistriesSearchByFilters(RegistrySilEventType.pivotSILChiediAccertamento, OffsetDateTime.now(),OffsetDateTime.now(),"orgFiscalCode","iuv",0,10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {},
      registriesApisHolder::unload);
  }
}
