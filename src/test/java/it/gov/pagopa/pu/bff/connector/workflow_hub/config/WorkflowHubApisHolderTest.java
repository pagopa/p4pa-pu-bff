package it.gov.pagopa.pu.bff.connector.workflow_hub.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
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
class WorkflowHubApisHolderTest extends BaseApiHolderTest {
  private WorkflowHubApisHolder workflowHubApisHolder;
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    WorkflowHubApiClientConfig clientConfig = WorkflowHubApiClientConfig.builder()
      .baseUrl("http://example.com")
      .build();
    workflowHubApisHolder = new WorkflowHubApisHolder(clientConfig, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(restTemplateBuilderMock, restTemplateMock);
  }

  @Test
  void whenGetTaxonomyApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> workflowHubApisHolder.getTaxonomyApi(accessToken)
        .synchronizeTaxonomy(),
      new ParameterizedTypeReference<>() {},
      workflowHubApisHolder::unload);
  }

  @Test
  void whenGetScheduleApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> workflowHubApisHolder.getScheduleApi(accessToken)
        .getScheduleInfo(ScheduleEnum.PAYMENTS_REPORTING_PAGOPA_BROKERS_FETCH),
      new ParameterizedTypeReference<>() {},
      workflowHubApisHolder::unload);
  }
}
