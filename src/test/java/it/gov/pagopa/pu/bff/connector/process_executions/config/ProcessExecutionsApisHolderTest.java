package it.gov.pagopa.pu.bff.connector.process_executions.config;

import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import java.time.LocalDateTime;
import java.util.List;
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
class ProcessExecutionsApisHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private ProcessExecutionsApisHolder apisHolder;
  private ProcessExecutionsApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = ProcessExecutionsApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new ProcessExecutionsApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getIngestionFlowFileSearchControllerApi(null));
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
      accessToken -> apisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
        .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(123L,
          List.of("ingestionFlowFileType"),
          LocalDateTime.now(),LocalDateTime.now(), IngestionFlowFileStatus.PROCESSING,"fileName","operatorExternalId",0,0,null),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetIngestionFlowFileSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
        .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(123L,
          List.of("ingestionFlowFileType"),
          LocalDateTime.now(),LocalDateTime.now(), IngestionFlowFileStatus.PROCESSING,"fileName","operatorExternalId",0,0,null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload
    );
  }

  @Test
  void whenGetExportFileSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getExportFileSearchControllerApi(accessToken)
        .crudExportFilesFindByOrganizationIDFlowTypeCreateDate(123L, "exportFileType",
          LocalDateTime.now(),LocalDateTime.now(),"operatorExternalId", ExportFileStatus.PROCESSING,"fileName",0,0,null),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload
    );
  }

  @Test
  void whenGetExportFileControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> {
        apisHolder.getExportFileControllerApi(accessToken)
          .createPaidExportFile(new PaidExportFileRequestDTO());
        return voidMock;
      },
      new ParameterizedTypeReference<>() {},
      apisHolder::unload
    );
  }
}

