package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
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
class ReceiptControllerTest {

  @Mock
  private ReceiptRetrieverService receiptRetrieverServiceMock;

  @InjectMocks
  private ReceiptController receiptController;

  private UserInfo userInfo;

  @BeforeEach
  void setUp() {
    userInfo = new UserInfo();
    userInfo.setMappedExternalUserId("fakeExternalUser");
    Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void givenCorrectRequestWhenGetReceiptsThenOk() {
    long organizationId = 1L;
    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;
    String iuv = "IUV123";
    String iur = "IUR123";
    String iud = "IUD123";
    long debtPositionTypeOrgId = 2L;
    Pageable pageable = PageRequest.of(0, 10);
    OffsetDateTime fromDate = OffsetDateTime.now().minusDays(10);
    OffsetDateTime toDate = OffsetDateTime.now();

    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(fromDate, toDate);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(organizationId, receiptOrigin, userInfo.getMappedExternalUserId(), iuv, iur, iud, debtPositionTypeOrgId, paymentDateTimeFilter);

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

    Mockito.when(receiptRetrieverServiceMock.getReceipts(filtersDTO, pageable, userInfo, "fakeAccessToken"))
      .thenReturn(expectedResult);

    ResponseEntity<PagedReceiptView> response = receiptController.getReceipts(organizationId, receiptOrigin, iuv, iur, iud, debtPositionTypeOrgId, fromDate, toDate, pageable);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

}

