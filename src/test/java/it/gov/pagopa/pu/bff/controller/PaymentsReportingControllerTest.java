package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.payments_reporting.PaymentsReportingRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReportingView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingControllerTest {

  @Mock
  private PaymentsReportingRetrieverService paymentsReportingRetrieverServiceMock;

  @InjectMocks
  private PaymentsReportingController paymentsReportingController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      paymentsReportingRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetPaymentsReportingThenOk() {
    long organizationId = 1L;
    String iuf = "IUF123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDate regulationDateFrom = LocalDate.now().minusDays(10);
    LocalDate regulationDateTo = LocalDate.now();
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);

    PagedPaymentsReportingView expectedResult = new PagedPaymentsReportingView();
    expectedResult.setContent(List.of(PaymentsReportingView.builder()
      .ingestionFlowFileId(123L)
      .organizationId(organizationId)
      .regulationUniqueIdentifier(regulationUniqueIdentifier)
      .regulationDate(LocalDate.now())
      .flowDateTime(OffsetDateTime.now())
      .totalPayments(100L)
      .iuf(iuf)
      .totalAmountCents(1000L)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(1L);
    expectedResult.setNumber(0L);

    Mockito.when(paymentsReportingRetrieverServiceMock.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter,
        pageable, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedPaymentsReportingView> response = paymentsReportingController.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFrom, regulationDateTo, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetPaymentsReportingRowsThenOk() {
    long organizationId = 1L;
    String iuf = "iuf";
    String iuv = "iuv";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(payDateFrom,payDateTo);

    PagedPaymentsReportingRow expectedResult = new PagedPaymentsReportingRow();

    Mockito.when(paymentsReportingRetrieverServiceMock.getPaymentsReportingRows(organizationId, iuf, iuv, payDateFilter,
        pageable, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedPaymentsReportingRow> response = paymentsReportingController.getPaymentsReportingRows(organizationId, iuf, iuv, payDateFrom, payDateTo, pageable);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetPaymentsReportingDetailThenOk() {
    long organizationId = 1L;
    String iuf = "iuf";
    String paymentsReportingId = "PAYREP1";

    PaymentsReportingDetailDTO expectedResult = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReportingId)
      .build();

    Mockito.when(paymentsReportingRetrieverServiceMock.getPaymentsReportingDetail(organizationId, iuf, paymentsReportingId, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PaymentsReportingDetailDTO> response = paymentsReportingController.getPaymentsReportingDetail(organizationId, iuf, paymentsReportingId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertSame(expectedResult, response.getBody());
  }
}
