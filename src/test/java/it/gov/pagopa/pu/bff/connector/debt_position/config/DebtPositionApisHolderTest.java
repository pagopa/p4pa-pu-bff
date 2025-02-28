package it.gov.pagopa.pu.bff.connector.debt_position.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
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
      DebtPositionType.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeWithCountSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypesWithCountFindByBrokerId(1L, 0, 0, Collections.emptyList()),
      PagedModelDebtPositionTypeWithCount.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetReceiptViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getReceiptViewSearchControllerApi(accessToken)
        .crudReceiptsViewFindReceiptsByFilters("1", "origin", "operator", "iuv", "iur", "iud", 1L, null, null, 0, 10, Collections.emptyList()),
      PagedModelReceiptView.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs("1", "operator123"),
      CollectionModelDebtPositionTypeOrg.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetReceiptApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getReceiptApi(accessToken)
        .getReceiptDetail(1L, "operatorExternalUserId"),
      ReceiptDetailDTO.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetInstallmentApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getInstallmentViewSearchControllerApi(accessToken)
        .crudInstallmentViewsFindInstallmentsByFilters(1L, "operatorExternalUserId", OffsetDateTime.now().minusDays(30), OffsetDateTime.now(), "iuv", "fiscalCode", 2L, 0, 10, Collections.emptyList()),
      PagedModelInstallmentView.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionViewSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionViewSearchControllerApi(accessToken)
        .crudDebtPositionsViewFindDebtPositionViews(
          1L,
          List.of("debtPositionOrigin"),
          "operatorExternalUserId",
          LocalDateTime.now(),
          LocalDateTime.now(),
          "fiscalCode",
          1L,
          "status", 0, 10, Collections.emptyList()),
      PagedModelDebtPositionView.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionApi(accessToken)
        .getDebtPosition(
          1L),
      DebtPositionDTO.class, debtPositionApisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> debtPositionApisHolder.getDebtPositionTypeOrgEntityControllerApi(accessToken)
        .crudGetDebtpositiontypeorg(
          "1"),
      DebtPositionTypeOrg.class, debtPositionApisHolder::unload);
  }
}

