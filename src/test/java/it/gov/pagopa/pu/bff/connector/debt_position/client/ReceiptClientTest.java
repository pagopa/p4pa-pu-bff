package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.controller.generated.ReceiptApi;
import it.gov.pagopa.pu.debtpositions.controller.generated.ReceiptViewSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

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

    List<ReceiptOriginType> receiptOrigins = List.of(ReceiptOriginType.RECEIPT_PAGOPA);
    String iuv = "iuv123";
    String iur = "iur123";
    String iud = "iud123";
    Long debtPositionTypeOrgId = 1L;
    String fiscalCode = "KNILSE99D44G600L";
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(
      1L, receiptOrigins, "operator", iuv, iur, iud, debtPositionTypeOrgId, paymentDateTimeFilter, fiscalCode);
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    when(debtPositionApisHolderMock.getReceiptViewSearchControllerApi(accessToken))
      .thenReturn(receiptViewSearchControllerApiMock);

    when(receiptViewSearchControllerApiMock.crudReceiptsViewFindReceiptsByFilters(
      String.valueOf(filtersDTO.getOrganizationId()),
      filtersDTO.getOperatorExternalUserId(),
      filtersDTO.getReceiptOrigins(),
      filtersDTO.getIuv(),
      filtersDTO.getIur(),
      filtersDTO.getIud(),
      filtersDTO.getDebtPositionTypeOrgId(),
      paymentDateTimeFrom,
      paymentDateTimeTo,
      filtersDTO.getFiscalCode(),
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
    Long organizationId = 1L;
    ReceiptDetailDTO expectedResult = new ReceiptDetailDTO();

    when(debtPositionApisHolderMock.getReceiptApi(accessToken))
      .thenReturn(receiptApiMock);
    when(receiptApiMock.getReceiptDetail(receiptId, organizationId, operatorExternalUserId))
      .thenReturn(expectedResult);

    ReceiptDetailDTO result = receiptClient.getReceiptDetail(receiptId, operatorExternalUserId, organizationId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenExceptionWhenGetReceiptDetailThenReturnNull() {
    Long receiptId = 123L;
    String operatorExternalUserId = "operatorExternalUserId";
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getReceiptApi(accessToken))
      .thenReturn(receiptApiMock);
    when(receiptApiMock.getReceiptDetail(receiptId, organizationId, operatorExternalUserId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ReceiptDetailDTO result = receiptClient.getReceiptDetail(receiptId, operatorExternalUserId, organizationId, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenReceiptPdfThenOk() {
    Long receiptId = 123L;
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";

    ByteArrayResource expectedResource = new ByteArrayResource("PDF-DATA".getBytes());
    String expectedFileName = "filename";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(
      ContentDisposition.attachment().filename(expectedFileName).build());
    ResponseEntity<Resource> responseEntity = new ResponseEntity<>(expectedResource, headers, HttpStatus.OK);

    when(debtPositionApisHolderMock.getReceiptApi(accessToken))
      .thenReturn(receiptApiMock);
    when(receiptApiMock.getReceiptPdfWithHttpInfo(receiptId,organizationId)).thenReturn(responseEntity);

    FileResourceDTO response = receiptClient.getReceiptPdf(receiptId, organizationId, accessToken);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResource,response.getResource());
    Assertions.assertEquals(expectedFileName,response.getFileName());
  }

  @Test
  void whenReceiptPdfThenNull() {
    Long receiptId = 123L;
    Long organizationId = 1L;
    String accessToken = "ACCESSTOKEN";

    when(debtPositionApisHolderMock.getReceiptApi(accessToken))
      .thenReturn(receiptApiMock);
    when(receiptApiMock.getReceiptPdfWithHttpInfo(receiptId, organizationId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    FileResourceDTO response = receiptClient.getReceiptPdf(receiptId, organizationId, accessToken);

    Assertions.assertNull(response);
  }
}

