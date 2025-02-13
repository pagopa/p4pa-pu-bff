package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.ReceiptViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
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
    String accessToken = "ACCESSTOKEN";
    PagedModelReceiptView expectedResult = new PagedModelReceiptView();

    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, "operator", "iuv", "iur", "iud", 1L, null, null);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filtersDTO.getOrganizationId()), filtersDTO.getReceiptOrigin().toString(), filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getIuv(), filtersDTO.getIur(), filtersDTO.getIud(), filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getFromDate(), filtersDTO.getToDate(),
      PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelReceiptView result = receiptClient.getReceipts(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNoReceiptsFoundWhenGetReceiptsThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, "operator", "iuv", "iur", "iud", 1L, null, null);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filtersDTO.getOrganizationId()), filtersDTO.getReceiptOrigin().toString(), filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getIuv(), filtersDTO.getIur(), filtersDTO.getIud(), filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getFromDate(), filtersDTO.getToDate(),
      PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    PagedModelReceiptView result = receiptClient.getReceipts(filtersDTO, pageable, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void givenGenericHttpExceptionWhenGetReceiptsThenThrowIt() {
    String accessToken = "ACCESSTOKEN";
    HttpClientErrorException expectedException = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, "operator", "iuv", "iur", "iud", 1L, null, null);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filtersDTO.getOrganizationId()), filtersDTO.getReceiptOrigin().toString(), filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getIuv(), filtersDTO.getIur(), filtersDTO.getIud(), filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getFromDate(), filtersDTO.getToDate(),
      PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenThrow(expectedException);

    HttpClientErrorException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> receiptClient.getReceipts(filtersDTO, pageable, accessToken));

    Assertions.assertSame(expectedException, result);
  }

  @Test
  void givenGenericExceptionWhenGetReceiptsThenThrowIt() {
    String accessToken = "ACCESSTOKEN";
    RuntimeException expectedException = new RuntimeException();
    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, "operator", "iuv", "iur", "iud", 1L, null, null);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);
    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filtersDTO.getOrganizationId()), filtersDTO.getReceiptOrigin().toString(), filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getIuv(), filtersDTO.getIur(), filtersDTO.getIud(), filtersDTO.getDebtPositionTypeOrgId(),
      filtersDTO.getFromDate(), filtersDTO.getToDate(),
      PageUtils.getPageNumber(pageable), PageUtils.getPageSize(pageable), PageUtils.getSortList(pageable)))
      .thenThrow(expectedException);

    RuntimeException result = Assertions.assertThrows(
      expectedException.getClass(),
      () -> receiptClient.getReceipts(filtersDTO, pageable, accessToken));

    Assertions.assertSame(expectedException, result);
  }

}

