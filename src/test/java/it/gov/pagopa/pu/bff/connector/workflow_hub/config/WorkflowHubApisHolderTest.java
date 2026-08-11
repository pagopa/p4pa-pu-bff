package it.gov.pagopa.pu.bff.connector.workflow_hub.config;

import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkflowHubApisHolderTest extends BaseApiHolderTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private WorkflowHubApisHolder apisHolder;
  private WorkflowHubApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = WorkflowHubApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new WorkflowHubApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getTaxonomyApi(null));
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(restTemplateBuilderMock, restTemplateMock);
  }

  @Test
  void testRetryConfiguration() {
    assertRetry(apiClientConfig,
      accessToken -> apisHolder.getTaxonomyApi(accessToken)
        .synchronizeTaxonomy(),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetTaxonomyApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTaxonomyApi(accessToken)
        .synchronizeTaxonomy(),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetScheduleApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getScheduleApi(accessToken)
        .getScheduleInfo(ScheduleEnum.PAYMENTS_REPORTING_PAGOPA_BROKERS_FETCH),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }
}
