package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptFilterDTO;
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
    ReceiptFilterDTO filter = new ReceiptFilterDTO();
    filter.setOrganizationId(1L);
    filter.setReceiptOrigin("origin");
    filter.setOperatorExternalUserId("operator");
    filter.setIuv("iuv");
    filter.setIur("iur");
    filter.setIud("iud");
    filter.setDebtPositionTypeOrgId(1L);
    filter.setFromDate(null);
    filter.setToDate(null);

    String accessToken = "ACCESSTOKEN";
    PagedModelReceiptView expectedResult = new PagedModelReceiptView();

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filter.getOrganizationId()), filter.getReceiptOrigin(), filter.getOperatorExternalUserId(),
      filter.getIuv(), filter.getIur(), filter.getIud(), filter.getDebtPositionTypeOrgId(), filter.getFromDate(),
      filter.getToDate(), 0, 10, Collections.emptyList()))
      .thenReturn(expectedResult);

    PagedModelReceiptView result = receiptClient.getReceipts(
      filter, PageRequest.of(0, 10, Sort.unsorted()), accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoReceiptsFoundWhenGetReceiptsThenReturnNull() {
    // Prepare the filter DTO instead of individual parameters
    ReceiptFilterDTO filter = new ReceiptFilterDTO();
    filter.setOrganizationId(1L);
    filter.setReceiptOrigin("origin");
    filter.setOperatorExternalUserId("operator");
    filter.setIuv("iuv");
    filter.setIur("iur");
    filter.setIud("iud");
    filter.setDebtPositionTypeOrgId(1L);
    filter.setFromDate(null);
    filter.setToDate(null);

    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filter.getOrganizationId()), filter.getReceiptOrigin(), filter.getOperatorExternalUserId(),
      filter.getIuv(), filter.getIur(), filter.getIud(), filter.getDebtPositionTypeOrgId(), filter.getFromDate(),
      filter.getToDate(), 0, 10, Collections.emptyList()))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    PagedModelReceiptView result = receiptClient.getReceipts(
      filter, PageRequest.of(0, 10, Sort.unsorted()), accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetReceiptsThenThrowIt() {
    ReceiptFilterDTO filter = new ReceiptFilterDTO();
    filter.setOrganizationId(1L);
    filter.setReceiptOrigin("origin");
    filter.setOperatorExternalUserId("operator");
    filter.setIuv("iuv");
    filter.setIur("iur");
    filter.setIud("iud");
    filter.setDebtPositionTypeOrgId(1L);
    filter.setFromDate(null);
    filter.setToDate(null);

    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filter.getOrganizationId()), filter.getReceiptOrigin(), filter.getOperatorExternalUserId(),
      filter.getIuv(), filter.getIur(), filter.getIud(), filter.getDebtPositionTypeOrgId(), filter.getFromDate(),
      filter.getToDate(), 0, 10, Collections.emptyList()))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> receiptClient.getReceipts(
        filter, PageRequest.of(0, 10, Sort.unsorted()), accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetReceiptsThenThrowIt() {
    ReceiptFilterDTO filter = new ReceiptFilterDTO();
    filter.setOrganizationId(1L);
    filter.setReceiptOrigin("origin");
    filter.setOperatorExternalUserId("operator");
    filter.setIuv("iuv");
    filter.setIur("iur");
    filter.setIud("iud");
    filter.setDebtPositionTypeOrgId(1L);
    filter.setFromDate(null);
    filter.setToDate(null);

    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filter.getOrganizationId()), filter.getReceiptOrigin(), filter.getOperatorExternalUserId(),
      filter.getIuv(), filter.getIur(), filter.getIud(), filter.getDebtPositionTypeOrgId(), filter.getFromDate(),
      filter.getToDate(), 0, 10, Collections.emptyList()))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> receiptClient.getReceipts(
        filter, PageRequest.of(0, 10, Sort.unsorted()), accessToken));

    Assertions.assertSame(expectedException, result);
  }

}
