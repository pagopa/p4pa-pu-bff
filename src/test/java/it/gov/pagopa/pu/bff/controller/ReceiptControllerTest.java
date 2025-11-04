package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReceiptControllerTest {

  @Mock
  private ReceiptRetrieverService receiptRetrieverServiceMock;

  @InjectMocks
  private ReceiptController receiptController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      receiptRetrieverServiceMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void givenCorrectRequestWhenGetReceiptsThenOk() {
    long organizationId = 1L;
    List<ReceiptOriginType> receiptOrigins = List.of(ReceiptOriginType.RECEIPT_PAGOPA);
    String iuv = "IUV123";
    String iur = "IUR123";
    String iud = "IUD123";
    long debtPositionTypeOrgId = 2L;
    Pageable pageable = PageRequest.of(0, 10);
    OffsetDateTime fromDate = OffsetDateTime.now().minusDays(10);
    OffsetDateTime toDate = OffsetDateTime.now();
    String fiscalCode = "FRTMRA90C41F205D";

    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(fromDate, toDate);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(
      organizationId, receiptOrigins, loggedUser.getMappedExternalUserId(), iuv, iur, iud, debtPositionTypeOrgId, paymentDateTimeFilter, fiscalCode);

    PagedReceiptView expectedResult = new PagedReceiptView();
    expectedResult.setContent(List.of(ReceiptView.builder()
      .receiptId(100L)
      .paymentAmountCents(1000L)
      .paymentDateTime(OffsetDateTime.now())
      .receiptOrigin(ReceiptOriginType.RECEIPT_PAGOPA)
      .iuv(iuv)
      .installmentId(200L)
      .debtPositionTypeOrgDescription("Description")
      .debtorFiscalCodeHash(new byte[]{1, 2, 3})
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(1L);
    expectedResult.setNumber(0L);

    Mockito.when(receiptRetrieverServiceMock.getReceipts(filtersDTO, pageable, loggedUser, accessToken))
      .thenReturn(expectedResult);

    ResponseEntity<PagedReceiptView> response = receiptController.getReceipts(
      organizationId, receiptOrigins, iuv, iur, iud, debtPositionTypeOrgId, fiscalCode, fromDate, toDate, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void givenCorrectRequestWhenGetReceiptDetailThenOk() {
    long organizationId = 1L;
    long receiptId = 2L;
    ReceiptDetailDTO expectedResult = new ReceiptDetailDTO();

    Mockito.when(receiptRetrieverServiceMock.getReceiptDetail(Mockito.eq(organizationId),Mockito.eq(receiptId),
        Mockito.same(loggedUser), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<ReceiptDetailDTO> response = receiptController.getReceiptDetail(organizationId,receiptId);

    Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult,response.getBody());
    Mockito.verify(receiptRetrieverServiceMock).getReceiptDetail(Mockito.eq(organizationId),Mockito.eq(receiptId),
      Mockito.any(), Mockito.anyString());
  }

  @Test
  void givenNoReceiptWhenGetReceiptDetailThenNotFound() {
    long organizationId = 1L;
    long receiptId = 2L;

    ResponseEntity<ReceiptDetailDTO> response = receiptController.getReceiptDetail(organizationId,receiptId);

    Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    Assertions.assertNull(response.getBody());
    Mockito.verify(receiptRetrieverServiceMock).getReceiptDetail(Mockito.eq(organizationId),Mockito.eq(receiptId),
      Mockito.any(), Mockito.anyString());
  }


  @Test
  void givenCorrectRequestWhenGetReceiptPdfThenOk() {
    Long organizationId = 1L;
    Long receiptId = 2L;
    FileResourceDTO fileResourceDTO = new FileResourceDTO();
    fileResourceDTO.setResource(new ByteArrayResource("PDF-DATA".getBytes()));
    fileResourceDTO.setFileName("filename");

    ResponseEntity<Resource> response = receiptController.getReceiptPdf(organizationId,receiptId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(MediaType.APPLICATION_PDF,response.getHeaders().getContentType());
    assertNotNull(response.getBody());
    assertEquals(fileResourceDTO.getResource(), response.getBody());
    assertEquals(fileResourceDTO.getFileName(), response.getHeaders().getContentDisposition().getFilename());
  }
}

