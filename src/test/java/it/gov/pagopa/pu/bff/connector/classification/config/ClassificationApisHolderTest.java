package it.gov.pagopa.pu.bff.connector.classification.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus;
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

import java.time.LocalDate;
import java.util.Collections;

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
      new ParameterizedTypeReference<>() {
      },
      classificationApisHolder::unload);
  }

  @Test
  void whenGetPaymentsReportingSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getPaymentsReportingSearchControllerApi(accessToken)
        .crudPaymentsReportingFindPaymentsReportingByFilters(1L, "IUF123", "iuv", null, null, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      classificationApisHolder::unload);
  }

  @Test
  void whenGetTreasuryViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getTreasuryViewSearchControllerApi(accessToken)
        .crudTreasuriesViewFindTreasuriesByFilters(
          1L, "iuv123", "iuf123", 100L, LocalDate.now().minusDays(10),
          LocalDate.now().minusDays(5), "prov123", "provAe123", "code123",
          "2025", "lastName", LocalDate.now().minusDays(5), LocalDate.now(),
          "doc123", "2025", 0, 10, Collections.emptyList()
        ),
      new ParameterizedTypeReference<>() {},
      classificationApisHolder::unload
    );
  }
  @Test
  void whenGetTreasurySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getTreasurySearchControllerApi(accessToken)
        .crudTreasuryFindByOrganizationIdAndTreasuryId(1L, "111"),
      new ParameterizedTypeReference<>() {
      },
      classificationApisHolder::unload);
  }

  @Test
  void whenGetClassificationsApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getClassificationsApi(accessToken)
        .getClassificationDetail(1L, 1L),
      new ParameterizedTypeReference<>() {
      },
      classificationApisHolder::unload);
  }

  @Test
  void whenGetAssessmentsRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> classificationApisHolder.getAssessmentsRegistrySearchControllerApi(accessToken)
        .crudAssessmentsRegistriesFindAssessmentsRegistriesByFilters(
                1L,
                Collections.singleton("code"),"sectionCode","sectionDescription",
                "officeCode","officeDescription","assessmentCode", "assessmentDescription",
                "operatingYear", AssessmentsRegistryStatus.ACTIVE, 0, 0, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      classificationApisHolder::unload);
  }
}
