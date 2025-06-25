package it.gov.pagopa.pu.bff.connector.registries.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
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
  void whenGetSilRegistryApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> registriesApisHolder.getSilRegistryApi(accessToken)
        .getSilRegistry("666"),
      new ParameterizedTypeReference<>() {},
      registriesApisHolder::unload);
  }
}
