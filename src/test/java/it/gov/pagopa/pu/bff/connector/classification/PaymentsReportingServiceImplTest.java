package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingSearchClient;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingViewSearchClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingServiceImplTest {

  @Mock
  private PaymentsReportingViewSearchClient paymentsReportingViewSearchClientMock;
  @Mock
  private PaymentsReportingSearchClient paymentsReportingSearchClientMock;

  private PaymentsReportingService service;

  @BeforeEach
  void setUp() {
    service = new PaymentsReportingServiceImpl(
      paymentsReportingViewSearchClientMock,paymentsReportingSearchClientMock);
  }

  @Test
  void whenGetPaymentsReportingThenInvokeClient() {
    Long organizationId = 1L;
    String iuf = "IUF123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(LocalDate.now().minusDays(10), LocalDate.now());
    Pageable pageable = Mockito.mock(Pageable.class);
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReportingView expectedResult = new PagedModelPaymentsReportingView();

    when(
      paymentsReportingViewSearchClientMock.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken))
      .thenReturn(expectedResult);

    PagedModelPaymentsReportingView result = service.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetPaymentsReportingDetailThenInvokeClient() {
    Long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "RUI123";
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(LocalDate.now().minusDays(10), LocalDate.now());
    Pageable pageable = Mockito.mock(Pageable.class);
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReporting expectedResult = new PagedModelPaymentsReporting();

    when(paymentsReportingSearchClientMock.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, accessToken))
      .thenReturn(expectedResult);

    PagedModelPaymentsReporting result = service.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
