package it.gov.pagopa.pu.bff.connector.classification.config;

import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationApisHolderTest extends BaseApiHolderTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private ClassificationApisHolder apisHolder;
  private ClassificationApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = ClassificationApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new ClassificationApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getPaymentsReportingViewSearchControllerApi(null));
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock);
  }

  @Test
  void testRetryConfiguration() {
    assertRetry(apiClientConfig,
      accessToken -> apisHolder.getPaymentsReportingViewSearchControllerApi(accessToken)
        .crudPaymentsReportingViewFindDistinctByIufAndRegulationUniqueIdentifier(1L, "IUF123", "RUI123", LocalDate.now(), LocalDate.now(), "IUV", 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetPaymentsReportingViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getPaymentsReportingViewSearchControllerApi(accessToken)
        .crudPaymentsReportingViewFindDistinctByIufAndRegulationUniqueIdentifier(1L, "IUF123", "RUI123", LocalDate.now(), LocalDate.now(), "IUV", 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetPaymentsReportingSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getPaymentsReportingWithReceiptViewSearchControllerApi(accessToken)
        .crudPaymentsReportingWithReceiptViewFindPaymentsReportingByFilters(1L, "IUF123", "iuv", LocalDate.now(), LocalDate.now(), "CODE123","FC123", 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetTreasuryViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTreasuryViewSearchControllerApi(accessToken)
        .crudTreasuriesViewFindTreasuriesByFilters(
          1L, "iuv123", "iuf123", 100L, LocalDate.now().minusDays(10),
          LocalDate.now().minusDays(5), "prov123", "provAe123", "code123",
          "2025", "lastName", LocalDate.now().minusDays(5), LocalDate.now(),
          "doc123", "2025", 0, 10, Collections.emptyList()
        ),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload
    );
  }

  @Test
  void whenGetTreasurySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTreasurySearchControllerApi(accessToken)
        .crudTreasuryFindByOrganizationIdAndTreasuryId(1L, "111"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetClassificationsApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getClassificationsApi(accessToken)
        .getClassificationDetail(1L, 1L),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsRegistrySearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsRegistrySearchControllerApi(accessToken)
        .crudAssessmentsRegistriesFindAssessmentsRegistriesByFilters(
          1L,
          Collections.singleton("code"), "sectionCode", "sectionDescription",
          "officeCode", "officeDescription", "assessmentCode", "assessmentDescription",
          "operatingYear", AssessmentsRegistryStatus.ACTIVE, 0, 0, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsRegistryEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsRegistryEntityControllerApi(accessToken)
        .crudGetAssessmentsregistry("1"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsRegistryApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsRegistryApi(accessToken)
        .createAssessmentsRegistry(new AssessmentsRegistry()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsControllerApi(accessToken)
        .getPagedAssessmentsList(1L, "assessmentName", OffsetDateTime.now(), OffsetDateTime.now() , "iuv", List.of("code"), AssessmentStatus.ACTIVE, 0, 1, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsDetailSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsDetailSearchControllerApi(accessToken)
        .crudAssessmentsDetailsFindAssessmentsRowsDetail(1L, "iud", "iuv",  LocalDateTime.now(), LocalDateTime.now() , OffsetDateTime.now(), OffsetDateTime.now(), "fiscalCode", 1,1, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsDetailEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsDetailEntityControllerApi(accessToken)
        .crudGetAssessmentsdetail("1"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsEntityControllerApi(accessToken)
        .crudGetAssessments("1"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsDetailApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getAssessmentsDetailApi(accessToken)
        .createAssessmentsDetail(1L,2L,new CreateAssessmentsDetail()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetAssessmentsEntityExtendedControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> {
        apisHolder.getAssessmentsEntityExtendedControllerApi(accessToken)
                .updateStatus(1L,2L,AssessmentStatus.ACTIVE);
        return voidMock;
      },
            new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetClassificationSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getClassificationSearchControllerApi(accessToken)
        .crudClassificationsFindByFilters(1L, "iuv", "iuf", List.of("code"), Collections.emptyList(), 1,1, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }
}
