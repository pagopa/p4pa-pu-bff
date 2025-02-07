package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.controller.generated.ReceiptViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private ReceiptViewSearchControllerApi receiptViewSearchControllerApiMock;

  private ReceiptClient receiptClient;

  @BeforeEach
  void setUp() {
    receiptClient = new ReceiptClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock);
  }

  @Test
  void whenGetReceiptsThenInvokeWithAccessToken() {
    long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    PagedModelReceiptView expectedResult = new PagedModelReceiptView();

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(organizationId), "origin", "operator", "iuv", "iur", "iud", 1L, null, null, 0, 10, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelReceiptView result = receiptClient.getReceipts(
      organizationId, "origin", "operator", "iuv", "iur", "iud", 1L, null, null,
      PageRequest.of(0, 10, Sort.unsorted()), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoReceiptsFoundWhenGetReceiptsThenReturnNull() {
    long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(organizationId), "origin", "operator", "iuv", "iur", "iud", 1L, null, null, 0, 10, Collections.emptyList()))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    PagedModelReceiptView result = receiptClient.getReceipts(
      organizationId, "origin", "operator", "iuv", "iur", "iud", 1L, null, null,
      PageRequest.of(0, 10, Sort.unsorted()), accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetReceiptsThenThrowIt() {
    long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(organizationId), "origin", "operator", "iuv", "iur", "iud", 1L, null, null, 0, 10, Collections.emptyList()))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> receiptClient.getReceipts(
        organizationId, "origin", "operator", "iuv", "iur", "iud", 1L, null, null,
        PageRequest.of(0, 10, Sort.unsorted()), accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetReceiptsThenThrowIt() {
    long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(organizationId), "origin", "operator", "iuv", "iur", "iud", 1L, null, null, 0, 10, Collections.emptyList()))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> receiptClient.getReceipts(
        organizationId, "origin", "operator", "iuv", "iur", "iud", 1L, null, null,
        PageRequest.of(0, 10, Sort.unsorted()), accessToken));

    Assertions.assertSame(expectedException, result);
  }

}

