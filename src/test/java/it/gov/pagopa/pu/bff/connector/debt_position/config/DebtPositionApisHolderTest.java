package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.bff.config.json.JsonConfig;
import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
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

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionApisHolderTest extends BaseApiHolderTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private DebtPositionApisHolder apisHolder;
  private DebtPositionApiClientConfig apiClientConfig;

  private static final OffsetDateTime FIXED_OFFSET_DATETIME_FROM =
    OffsetDateTime.of(2026, 4, 18, 12, 0, 0, 0, ZoneOffset.UTC);

  private static final OffsetDateTime FIXED_OFFSET_DATETIME_TO =
    OffsetDateTime.of(2026, 6, 18, 12, 0, 0, 0, ZoneOffset.UTC);

  private static final LocalDate FIXED_LOCAL_DATE_FROM =
    LocalDate.of(2026, Month.JUNE, 15);

  private static final LocalDate FIXED_LOCAL_DATE_TO =
    LocalDate.of(2026, Month.JUNE, 18);

  private static final LocalDateTime FIXED_LOCAL_DATETIME_FROM =
    LocalDateTime.of(2026, Month.JUNE, 18, 12, 0);

  private static final LocalDateTime FIXED_LOCAL_DATETIME_TO =
    LocalDateTime.of(2026, Month.JUNE, 18, 12, 0);

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = DebtPositionApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new DebtPositionApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getDebtPositionTypeControllerApi(null));
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
      accessToken -> apisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudGetDebtpositiontype(String.valueOf(123L)),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetDebtPositionTypeControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudGetDebtpositiontype(String.valueOf(123L)),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeWithCountSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypesWithCountFindByBrokerId(1L, "code", "description", 0, 0, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgWithCountSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsWithCountFindByCodeAndDescription(1L, "code", "description", true, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetReceiptViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getReceiptViewSearchControllerApi(accessToken)
        .crudReceiptsViewFindReceiptsByFilters(1L, "operator", List.of(ReceiptOriginType.RECEIPT_PAGOPA),  "iuv", "iur", "iud", 1L, FIXED_OFFSET_DATETIME_FROM, FIXED_OFFSET_DATETIME_TO, "fiscalCode", 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(1L, "operator123", true),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgCountByOrganizationIdSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsByOrganizationCountByOrganizationIds(List.of(1L)),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgId(1L),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void whenGetTransferApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTransferApi(accessToken)
        .validateTaxonomyCategory("001122233", "orgFiscalCode"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }

  @Test
  void whenGetTransferSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTransferSearchControllerApi(accessToken)
        .crudTransfersFindAuthorizedByInstallmentId(1L, "operatorExternalUserId"),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void whenGetReceiptApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getReceiptApi(accessToken)
        .getReceiptDetail(1L, 1L, "operatorExternalUserId", "iud"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetInstallmentApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getInstallmentApi(accessToken)
        .getInstallmentDetail(1L, "operatorExternalUserId"),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void whenGetInstallmentNoPiiSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode()
    throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getInstallmentNoPiiSearchControllerApi(
          accessToken)
        .crudInstallmentsFindAuthorizedByTransferSemanticKey(1L, "iuv", "iur",
          2, "operatorExternalUserId", List.of()),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void givenGetInstallmentsByFiltersWhenGetInstallmentApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getInstallmentApi(accessToken)
        .getInstallmentsByFilters(1L, "operatorExternalUserId", FIXED_LOCAL_DATE_FROM, FIXED_LOCAL_DATE_TO, "iuv", "iud", "fiscalCode", Collections.emptyList(), 2L, InstallmentStatus.PAID, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      }, apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionViewSearchControllerApi(accessToken)
        .crudDebtPositionsViewFindDebtPositionViews(
          1L,
          "operatorExternalUserId",
          List.of(DebtPositionOrigin.ORDINARY),
          FIXED_LOCAL_DATETIME_FROM,
          FIXED_LOCAL_DATETIME_TO,
          "fiscalCode",
          1L,
          DebtPositionStatus.PAID,
          "IUV123",
          "IUD123",
          0, 10,
          Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionApi(accessToken)
        .getDebtPosition(
          1L),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgEntityControllerApi(accessToken)
        .crudGetDebtpositiontypeorg(
          "1"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionSearchControllerApi(accessToken)
        .crudDebtPositionsFindByDebtPositionTypeOrgId(
          1L,1,1,Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> {
        apisHolder.getDebtPositionTypeOrgApi(accessToken)
          .deleteDebtPositionTypeOrg(
            1L);
        return voidMock;
      },
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeSearchControllerApi(accessToken)
        .crudDebtPositionTypesFindAllByBrokerIdAndOrgType(
          1L,"01"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(
          1L, Collections.emptySet()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgOperatorsApi(accessToken)
        .deleteOperators(1L, Set.of("operator1")),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetSpontaneousFormSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getSpontaneousFormSearchControllerApi(accessToken)
        .crudSpontaneousFormsFindAllByOrganizationId(1L),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetSpontaneousFormEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getSpontaneousFormEntityControllerApi(accessToken)
        .crudGetSpontaneousform("1"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetSpontaneousFormApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getSpontaneousFormApi(accessToken)
        .createSpontaneousForm(new SpontaneousForm()),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYear(1L, "2026"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }
}

