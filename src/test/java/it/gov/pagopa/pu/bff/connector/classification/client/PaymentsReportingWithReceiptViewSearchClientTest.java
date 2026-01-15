package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingWithReceiptViewSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingWithReceiptViewSearchClientTest {
  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private PaymentsReportingWithReceiptViewSearchControllerApi paymentsReportingWithReceiptViewSearchControllerApi;

  private PaymentsReportingWithReceiptViewSearchClient paymentsReportingWithReceiptViewSearchClient;

  @BeforeEach
  void setUp() {
    paymentsReportingWithReceiptViewSearchClient = new PaymentsReportingWithReceiptViewSearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,
      paymentsReportingWithReceiptViewSearchControllerApi
    );
  }

  @Test
  void whenGetPaymentsReportingThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReportingWithReceiptView expectedResult = new PagedModelPaymentsReportingWithReceiptView();

    Long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "iuv";
    String debtPositionTypeOrgCode = "DPT123";
    String debtorFiscalCode = "DF123";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(classificationApisHolderMock.getPaymentsReportingWithReceiptViewSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingWithReceiptViewSearchControllerApi);

    when(paymentsReportingWithReceiptViewSearchControllerApi.crudPaymentsReportingWithReceiptViewFindPaymentsReportingByFilters(
      organizationId,
      iuf,
      iuv,
      payDateFilter.getFrom(),
      payDateFilter.getTo(),
      debtPositionTypeOrgCode,
      debtorFiscalCode,
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelPaymentsReportingWithReceiptView result = paymentsReportingWithReceiptViewSearchClient.getPaymentsReportingRows(organizationId, iuf, iuv, payDateFilter, debtPositionTypeOrgCode, debtorFiscalCode, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
