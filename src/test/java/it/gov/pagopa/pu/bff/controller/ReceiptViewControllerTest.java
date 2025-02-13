package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.service.receipts.ReceiptViewService;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.OffsetDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReceiptViewControllerTest {

  @Mock
  private ReceiptViewService receiptViewServiceMock;

  @InjectMocks
  private ReceiptViewController receiptViewController;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetReceiptsThenOk() {
    long organizationId = 1L;
    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;
    String iuv = "IUV123";
    Pageable pageable = PageRequest.of(0, 10);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(organizationId, receiptOrigin, null, iuv, null, null, null, null, null);

    PagedReceiptView expectedResult = new PagedReceiptView();
    expectedResult.setContent(List.of(ReceiptView.builder()
      .receiptId(100L)
      .paymentAmountCents(1000L)
      .paymentDateTime(OffsetDateTime.now())
      .receiptOrigin(receiptOrigin)
      .iuv(iuv)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(1L);
    expectedResult.setNumber(0L);

    Mockito.when(receiptViewServiceMock.getReceipts(
      Mockito.eq(filtersDTO),
      Mockito.eq(pageable),
      Mockito.any(),
      Mockito.anyString()
    )).thenReturn(expectedResult);

    ResponseEntity<PagedReceiptView> response = receiptViewController.getReceipts(
      organizationId, receiptOrigin, null, iuv, null, null,
      null, null, null, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

}

