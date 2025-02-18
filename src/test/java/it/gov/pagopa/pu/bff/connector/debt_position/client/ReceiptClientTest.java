package it.gov.pagopa.pu.bff.connector.debt_position.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.ReceiptApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.ReceiptViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import java.time.OffsetDateTime;
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
class ReceiptClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private ReceiptViewSearchControllerApi receiptViewSearchControllerApiMock;
  @Mock
  private ReceiptApi receiptApiMock;

  private ReceiptClient receiptClient;

  @BeforeEach
  void setUp() {
    receiptClient = new ReceiptClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
      receiptViewSearchControllerApiMock
    );
  }

  @Test
  void whenGetReceiptsThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelReceiptView expectedResult = new PagedModelReceiptView();

    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;
    String iuv = "iuv123";
    String iur = "iur123";
    String iud = "iud123";
    Long debtPositionTypeOrgId = 1L;
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(
      1L, receiptOrigin, "operator", iuv, iur, iud, debtPositionTypeOrgId, paymentDateTimeFilter);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);

    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filtersDTO.getOrganizationId()),
      filtersDTO.getReceiptOrigin().toString(),
      filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getIuv(),
      filtersDTO.getIur(),
      filtersDTO.getIud(),
      filtersDTO.getDebtPositionTypeOrgId(),
      paymentDateTimeFrom,
      paymentDateTimeTo,
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelReceiptView result = receiptClient.getReceipts(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetReceiptDetailThenInvokeWithAccessToken() {
    Long receiptId = 123L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";
    ReceiptDetailDTO expectedResult = new ReceiptDetailDTO();

    when(debtPositionApisHolderMock.getReceiptApi(accessToken))
      .thenReturn(receiptApiMock);
    when(receiptApiMock.getReceiptDetail(receiptId,operatorExternalUserId))
      .thenReturn(expectedResult);

    ReceiptDetailDTO result = receiptClient.getReceiptDetail(receiptId, operatorExternalUserId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExceptionWhenGetReceiptDetailThenReturnNull() {
    Long receiptId = 123L;
    String operatorExternalUserId = "operatorExternalUserId";
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getReceiptApi(accessToken))
      .thenReturn(receiptApiMock);
    when(receiptApiMock.getReceiptDetail(receiptId,operatorExternalUserId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ReceiptDetailDTO result = receiptClient.getReceiptDetail(receiptId, operatorExternalUserId, accessToken);

    Assertions.assertNull(result);
  }
}

