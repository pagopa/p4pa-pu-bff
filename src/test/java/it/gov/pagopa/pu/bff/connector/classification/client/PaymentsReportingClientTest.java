package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingViewSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
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
class PaymentsReportingClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private PaymentsReportingViewSearchControllerApi paymentsReportingViewSearchControllerApiMock;

  private PaymentsReportingClient paymentsReportingClient;

  @BeforeEach
  void setUp() {
    paymentsReportingClient = new PaymentsReportingClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,
      paymentsReportingViewSearchControllerApiMock
    );
  }

  @Test
  void whenGetPaymentsReportingThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReportingView expectedResult = new PagedModelPaymentsReportingView();

    Long organizationId = 1L;
    String iuf = "IUF123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDate regulationDateFrom = LocalDate.now().minusDays(10);
    LocalDate regulationDateTo = LocalDate.now();
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(classificationApisHolderMock.getPaymentsReportingViewSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingViewSearchControllerApiMock);

    when(paymentsReportingViewSearchControllerApiMock.crudPaymentsReportingViewFindDistinctByIufAndRegulationUniqueIdentifier(
      String.valueOf(organizationId),
      iuf,
      regulationUniqueIdentifier,
      regulationDateFilter.getFrom(),
      regulationDateFilter.getTo(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelPaymentsReportingView result = paymentsReportingClient.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
