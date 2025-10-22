package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingViewSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingWithReceiptViewSearchClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingServiceImplTest {

  @Mock
  private PaymentsReportingViewSearchClient paymentsReportingViewSearchClientMock;
  @Mock
  private PaymentsReportingSearchClient paymentsReportingSearchClientMock;
  @Mock
  private PaymentsReportingWithReceiptViewSearchClient paymentsReportingWithReceiptViewSearchClientMock;

  private PaymentsReportingService service;

  @BeforeEach
  void setUp() {
    service = new PaymentsReportingServiceImpl(
      paymentsReportingViewSearchClientMock, paymentsReportingSearchClientMock, paymentsReportingWithReceiptViewSearchClientMock);
  }

  @Test
  void whenGetPaymentsReportingThenInvokeClient() {
    Long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "IUV123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(LocalDate.now().minusDays(10), LocalDate.now());
    Pageable pageable = Mockito.mock(Pageable.class);
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReportingView expectedResult = new PagedModelPaymentsReportingView();

    when(
      paymentsReportingViewSearchClientMock.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, iuv, pageable, accessToken))
      .thenReturn(expectedResult);

    PagedModelPaymentsReportingView result = service.getPaymentsReportingView(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, iuv, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetPaymentsReportingRowsThenInvokeClient() {
    Long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "RUI123";
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(LocalDate.now().minusDays(10), LocalDate.now());
    Pageable pageable = Mockito.mock(Pageable.class);
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReportingWithReceiptView expectedResult = new PagedModelPaymentsReportingWithReceiptView();

    when(paymentsReportingWithReceiptViewSearchClientMock.getPaymentsReportingRows(organizationId, iuf, iuv, payDateFilter, pageable, accessToken))
      .thenReturn(expectedResult);

    PagedModelPaymentsReportingWithReceiptView result = service.getPaymentsReportingRows(organizationId, iuf, iuv, payDateFilter, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetPaymentsReportingDetailThenInvokeClient() {
    Long organizationId = 1L;
    String paymentsReportingId = "PAYREP123";
    String accessToken = "ACCESSTOKEN";
    PaymentsReporting expectedResult = new PaymentsReporting();

    when(paymentsReportingSearchClientMock.getPaymentsReportingDetail(organizationId, paymentsReportingId, accessToken))
      .thenReturn(expectedResult);

    PaymentsReporting result = service.getPaymentsReportingDetail(organizationId, paymentsReportingId, accessToken);

    assertSame(expectedResult, result);
  }
}
