package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionStatus;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class DebtPositionApisHolderTest extends BaseApiHolderTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private DebtPositionApisHolder debtPositionApisHolder;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    DebtPositionApiClientConfig clientConfig = DebtPositionApiClientConfig.builder()
      .baseUrl("http://example.com")
      .build();
    debtPositionApisHolder = new DebtPositionApisHolder(clientConfig, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock);
  }

  @Test
  void whenGetDebtPositionTypeControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudGetDebtpositiontype(String.valueOf(123L)),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeWithCountSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypesWithCountFindByBrokerId(1L, "description", 0, 0, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgWithCountSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsWithCountFindByCodeAndDescription(1L, "code", "description", true, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetReceiptViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getReceiptViewSearchControllerApi(accessToken)
        .crudReceiptsViewFindReceiptsByFilters("1", "operator", List.of(ReceiptOriginType.RECEIPT_PAGOPA),  "iuv", "iur", "iud", 1L, null, null, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs("1", "operator123", true),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgCountByOrganizationIdSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgCountByOrganizationIdSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsByOrganizationCountByOrganizationIds(List.of(1L)),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgId(1L),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetTransferSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getTransferSearchControllerApi(accessToken)
        .crudTransfersFindAuthorizedByInstallmentId("1", "operatorExternalUserId"),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetReceiptApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getReceiptApi(accessToken)
        .getReceiptDetail(1L, "operatorExternalUserId"),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetInstallmentApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getInstallmentApi(accessToken)
        .getInstallmentDetail(1L, "operatorExternalUserId"),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetInstallmentNoPiiSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode()
    throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getInstallmentNoPiiSearchControllerApi(
          accessToken)
        .crudInstallmentsFindAuthorizedByTransferSemanticKey(1L, "iuv", "iur",
          "transferIndex", "operatorExternalUserId", null),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetInstallmentViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getInstallmentViewSearchControllerApi(accessToken)
        .crudInstallmentViewsFindInstallmentsByFilters(1L, "operatorExternalUserId", LocalDate.now().minusDays(30), LocalDate.now(), "iuv", "fiscalCode", 2L, 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      }, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionViewSearchControllerApi(accessToken)
        .crudDebtPositionsViewFindDebtPositionViews(
          1L,
          "operatorExternalUserId",
          List.of("debtPositionOrigin"),
          LocalDateTime.now(),
          LocalDateTime.now(),
          "fiscalCode",
          1L,
          DebtPositionStatus.PAID,
          "IUV123", 0, 10, Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionApi(accessToken)
        .getDebtPosition(
          1L),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgEntityControllerApi(accessToken)
        .crudGetDebtpositiontypeorg(
          "1"),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionSearchControllerApi(accessToken)
        .crudDebtPositionsFindByDebtPositionTypeOrgId(
          1L,1,1,Collections.emptyList()),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> {
        debtPositionApisHolder.getDebtPositionTypeOrgApi(accessToken)
          .deleteDebtPositionTypeOrg(
            1L);
        return voidMock;
      },
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeSearchControllerApi(accessToken)
        .crudDebtPositionTypesFindAllByBrokerIdAndOrgType(
          1L,"01"),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgOperatorsDptoCountViewSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgOperatorsCountViewFindByOrganizationIdAndOperatorExternalUserIds(
          1L, Collections.emptySet()),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgOperatorsApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgOperatorsApi(accessToken)
        .deleteOperators(1L, Set.of("operator1")),
      new ParameterizedTypeReference<>() {
      },
      debtPositionApisHolder::unload);
  }
}

