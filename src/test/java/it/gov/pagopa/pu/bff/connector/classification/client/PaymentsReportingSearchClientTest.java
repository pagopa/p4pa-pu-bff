package it.gov.pagopa.pu.bff.connector.classification.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingSearchClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private PaymentsReportingSearchControllerApi paymentsReportingSearchControllerApiMock;

  private PaymentsReportingSearchClient paymentsReportingSearchClient;

  @BeforeEach
  void setUp() {
    paymentsReportingSearchClient = new PaymentsReportingSearchClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,
      paymentsReportingSearchControllerApiMock
    );
  }

  @Test
  void whenGetPaymentsReportingThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReporting expectedResult = new PagedModelPaymentsReporting();

    Long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "iuv";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(classificationApisHolderMock.getPaymentsReportingSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingSearchControllerApiMock);

    when(paymentsReportingSearchControllerApiMock.crudPaymentsReportingFindPaymentsReportingByFilters(
      organizationId,
      iuf,
      iuv,
      payDateFilter.getFrom(),
      payDateFilter.getTo(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelPaymentsReporting result = paymentsReportingSearchClient.getPaymentsReportingRows(organizationId, iuf, iuv, payDateFilter, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetPaymentsReportingDetailThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PaymentsReporting expectedResult = new PaymentsReporting();

    Long organizationId = 1L;
    String paymentsReportingId = "PAYREP123";

    when(classificationApisHolderMock.getPaymentsReportingSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingSearchControllerApiMock);

    when(paymentsReportingSearchControllerApiMock.crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(
      organizationId, paymentsReportingId))
      .thenReturn(expectedResult);

    PaymentsReporting result = paymentsReportingSearchClient.getPaymentsReportingDetail(organizationId, paymentsReportingId, accessToken);

    assertSame(expectedResult, result);
  }


  @Test
  void givenNoPaymentsReportingWhenGetDebtPositionPaymentsReportingDetailThenReturnNull() {
    Long organizationId = 1L;
    String paymentsReportingId = "PAYREP123";
    String accessToken = "ACCESSTOKEN";

    when(classificationApisHolderMock.getPaymentsReportingSearchControllerApi(accessToken))
      .thenReturn(paymentsReportingSearchControllerApiMock);
    when(paymentsReportingSearchControllerApiMock.crudPaymentsReportingFindByOrganizationIdAndPaymentsReportingId(organizationId, paymentsReportingId))
      .thenThrow(
        HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    PaymentsReporting result = paymentsReportingSearchClient.getPaymentsReportingDetail(organizationId, paymentsReportingId,accessToken);

    Assertions.assertNull(result);
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock,paymentsReportingSearchControllerApiMock);
  }
}
