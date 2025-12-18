package it.gov.pagopa.pu.bff.connector.process_executions.config;

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

@ExtendWith(MockitoExtension.class)
class ProcessExecutionsApisHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private ProcessExecutionsApisHolder processExecutionsApisHolder;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    ProcessExecutionsApiClientConfig clientConfig = ProcessExecutionsApiClientConfig.builder()
      .baseUrl("http://example.com")
      .build();
    processExecutionsApisHolder = new ProcessExecutionsApisHolder(clientConfig, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock
    );
  }

  @Test
  void whenGetIngestionFlowFileSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> processExecutionsApisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
        .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(String.valueOf(123L),
          List.of("ingestionFlowFileType"),
          LocalDateTime.now(),LocalDateTime.now(), IngestionFlowFileStatus.PROCESSING,"fileName","operatorExternalId",0,0,null),
      new ParameterizedTypeReference<>() {},
      processExecutionsApisHolder::unload
    );
  }

  @Test
  void whenGetExportFileSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> processExecutionsApisHolder.getExportFileSearchControllerApi(accessToken)
        .crudExportFilesFindByOrganizationIDFlowTypeCreateDate(String.valueOf(123L), "exportFileType",
          LocalDateTime.now(),LocalDateTime.now(),"operatorExternalId", ExportFileStatus.PROCESSING,"fileName",0,0,null),
      new ParameterizedTypeReference<>() {},
      processExecutionsApisHolder::unload
    );
  }

  @Test
  void whenGetExportFileControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> {
        processExecutionsApisHolder.getExportFileControllerApi(accessToken)
          .createPaidExportFile(new PaidExportFileRequestDTO());
        return voidMock;
      },
      new ParameterizedTypeReference<>() {},
      processExecutionsApisHolder::unload
    );
  }
}

