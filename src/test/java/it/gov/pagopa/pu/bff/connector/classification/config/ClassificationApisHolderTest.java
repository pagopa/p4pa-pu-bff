package it.gov.pagopa.pu.bff.connector.classification.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import java.util.Collections;
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
class ClassificationApisHolderTest extends BaseApiHolderTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private ClassificationApisHolder classificationApisHolder;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    ClassificationApiClientConfig clientConfig = ClassificationApiClientConfig.builder()
      .baseUrl("http://example.com")
      .build();
    classificationApisHolder = new ClassificationApisHolder(clientConfig, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock);
  }

  @Test
  void whenGetPaymentsReportingViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getPaymentsReportingViewSearchControllerApi(accessToken)
        .crudPaymentsReportingViewFindDistinctByIufAndRegulationUniqueIdentifier("1", "IUF123", "RUI123", null, null, 0, 10, Collections.emptyList()),
      PagedModelPaymentsReportingView.class, classificationApisHolder::unload);
  }

  @Test
  void whenGetPaymentsReportingSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getPaymentsReportingSearchControllerApi(accessToken)
        .crudPaymentsReportingFindPaymentsReportingByFilters(1L, "IUF123", "iuv", null, null, 0, 10, Collections.emptyList()),
      PagedModelPaymentsReporting.class, classificationApisHolder::unload);
  }
}
