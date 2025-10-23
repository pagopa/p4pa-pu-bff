package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingMapper;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingViewMapper;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.service.payments_reporting.PaymentsReportingRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingWithReceiptView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonEntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingRetrieverServiceImplTest {

  @Mock
  private PaymentsReportingService paymentsReportingServiceMock;
  @Mock
  private InstallmentRetrieverService installmentRetrieverServiceMock;
  @Mock
  private ReceiptRetrieverService receiptRetrieverServiceMock;
  @Mock
  private PaymentsReportingViewMapper paymentsReportingViewMapperMock;
  @Mock
  private PaymentsReportingMapper paymentsReportingMapperMock;

  private PaymentsReportingRetrieverServiceImpl paymentsReportingRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    paymentsReportingRetrieverService = new PaymentsReportingRetrieverServiceImpl(
      paymentsReportingServiceMock, installmentRetrieverServiceMock,
      receiptRetrieverServiceMock, paymentsReportingViewMapperMock,
      paymentsReportingMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(paymentsReportingServiceMock,
      installmentRetrieverServiceMock, receiptRetrieverServiceMock,
      paymentsReportingViewMapperMock,
      paymentsReportingMapperMock);
  }

  @Test
  void givenValidUserWhenGetPaymentsReportingThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "IUV123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDate regulationDateFrom = LocalDate.now().minusDays(10);
    LocalDate regulationDateTo = LocalDate.now();
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(
      regulationDateFrom, regulationDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelPaymentsReportingView pagedModelPaymentsReportingView = new PagedModelPaymentsReportingView();
    PagedPaymentsReportingView expectedPagedPaymentsReportingView = new PagedPaymentsReportingView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenAnswer(a->null);

      when(
        paymentsReportingServiceMock.getPaymentsReporting(organizationId, iuf,
          regulationUniqueIdentifier, regulationDateFilter, iuv, pageable,
          accessToken))
        .thenReturn(pagedModelPaymentsReportingView);

      when(paymentsReportingViewMapperMock.mapToPagedPaymentsReporting(
        pagedModelPaymentsReportingView))
        .thenReturn(expectedPagedPaymentsReportingView);

      PagedPaymentsReportingView result = paymentsReportingRetrieverService.getPaymentsReporting(
        organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter,
        iuv, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedPaymentsReportingView, result);

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetPaymentsReportingThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "IUV123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDate regulationDateFrom = LocalDate.now().minusDays(10);
    LocalDate regulationDateTo = LocalDate.now();
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(
      regulationDateFrom, regulationDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        paymentsReportingRetrieverService.getPaymentsReporting(organizationId,
          iuf, regulationUniqueIdentifier, regulationDateFilter, iuv, pageable,
          loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenNoFiltersWhenGetPaymentsReportingThenThrowException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      assertThrows(IllegalArgumentException.class, () ->
        paymentsReportingRetrieverService.getPaymentsReporting(
          organizationId, null, null, null, null, pageable, loggedUser, accessToken));
    }
  }

  @Test
  void givenPartialDateRangeWhenGetPaymentsReportingThenThrowException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(LocalDate.now(), null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      assertThrows(IllegalArgumentException.class, () ->
        paymentsReportingRetrieverService.getPaymentsReporting(
          organizationId, null, null, regulationDateFilter, null, pageable, loggedUser, accessToken));
    }
  }

  @Test
  void givenCompleteDateRangeWhenGetPaymentsReportingThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(
      LocalDate.now().minusDays(5), LocalDate.now());

    PagedModelPaymentsReportingView pagedModelPaymentsReportingView = new PagedModelPaymentsReportingView();
    PagedPaymentsReportingView expected = new PagedPaymentsReportingView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(paymentsReportingServiceMock.getPaymentsReporting(
        organizationId, null, null, regulationDateFilter, null, pageable, accessToken))
        .thenReturn(pagedModelPaymentsReportingView);

      when(paymentsReportingViewMapperMock.mapToPagedPaymentsReporting(pagedModelPaymentsReportingView))
        .thenReturn(expected);

      PagedPaymentsReportingView result = paymentsReportingRetrieverService.getPaymentsReporting(
        organizationId, null, null, regulationDateFilter, null, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);
    }
  }

  @Test
  void givenOnlyIufWhenGetPaymentsReportingThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    String iuf = "IUF-001";

    PagedModelPaymentsReportingView pagedModelPaymentsReportingView = new PagedModelPaymentsReportingView();
    PagedPaymentsReportingView expected = new PagedPaymentsReportingView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(paymentsReportingServiceMock.getPaymentsReporting(
        organizationId, iuf, null, null, null, pageable, accessToken))
        .thenReturn(pagedModelPaymentsReportingView);

      when(paymentsReportingViewMapperMock.mapToPagedPaymentsReporting(pagedModelPaymentsReportingView))
        .thenReturn(expected);

      PagedPaymentsReportingView result = paymentsReportingRetrieverService.getPaymentsReporting(
        organizationId, iuf, null, null, null, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);
    }
  }

  @Test
  void givenMultipleFiltersWhenGetPaymentsReportingThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    String iuf = "IUF-001";
    String regulationUniqueIdentifier = "REG-123";
    String iuv = "IUV-123";
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(LocalDate.now().minusDays(10), LocalDate.now());

    PagedModelPaymentsReportingView pagedModelPaymentsReportingView = new PagedModelPaymentsReportingView();
    PagedPaymentsReportingView expected = new PagedPaymentsReportingView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(paymentsReportingServiceMock.getPaymentsReporting(
        organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, iuv, pageable, accessToken))
        .thenReturn(pagedModelPaymentsReportingView);

      when(paymentsReportingViewMapperMock.mapToPagedPaymentsReporting(pagedModelPaymentsReportingView))
        .thenReturn(expected);

      PagedPaymentsReportingView result = paymentsReportingRetrieverService.getPaymentsReporting(
        organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, iuv, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);
    }
  }

  @Test
  void givenValidUserWhenGetPaymentsReportingRowsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "iuv";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(
      payDateFrom, payDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelPaymentsReportingWithReceiptView pagedModelPaymentsReporting = new PagedModelPaymentsReportingWithReceiptView();
    PagedPaymentsReportingRow expectedResult = new PagedPaymentsReportingRow();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser)).thenAnswer(a -> null);

      when(paymentsReportingServiceMock.getPaymentsReportingRows(organizationId,
        iuf, iuv, payDateFilter, pageable, accessToken))
        .thenReturn(pagedModelPaymentsReporting);

      when(paymentsReportingMapperMock.mapToPagedPaymentsReporting(
        pagedModelPaymentsReporting))
        .thenReturn(expectedResult);

      PagedPaymentsReportingRow result = paymentsReportingRetrieverService.getPaymentsReportingRows(
        organizationId, iuf, iuv, payDateFilter, pageable, loggedUser,
        accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetPaymentsReportingRowsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "iuv";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(
      payDateFrom, payDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        paymentsReportingRetrieverService.getPaymentsReportingRows(
          organizationId, iuf, iuv, payDateFilter, pageable, loggedUser,
          accessToken));

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenValidUserWhenGetPaymentsReportingDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "iuf";
    String paymentsReportingId = "PAYREP1";

    String iuv = "IUV123";
    String iur = "IUR123";
    int transferIndex = 1;
    long receiptId = 1L;
    PaymentsReporting paymentsReporting = PaymentsReporting.builder()
      .paymentsReportingId(paymentsReportingId)
      .organizationId(organizationId)
      .iuv(iuv)
      .iur(iur)
      .transferIndex(transferIndex)
      // fields required non-null but not useful to this test
      .ingestionFlowFileId(1L)
      .pspIdentifier("PSPID")
      .iuf(iuf)
      .flowDateTime(OffsetDateTime.now())
      .regulationDate(LocalDate.now())
      .regulationUniqueIdentifier("REG123")
      .senderPspCode("SENDERCODE")
      .senderPspName("SENDERNAME")
      .senderPspType("SENDERTYPE")
      .receiverOrganizationCode("RECEIVERCODE")
      .receiverOrganizationName("RECEIVERNAME")
      .receiverOrganizationType("RECEIVERTYPE")
      .totalPayments(1000L)
      .totalAmountCents(1000L)
      .amountPaidCents(1000L)
      .paymentOutcomeCode("OUTCOMECODE")
      .payDate(LocalDate.now())
      .acquiringDate(LocalDate.now())
      .build();
    InstallmentNoPII installmentNoPII = InstallmentNoPII.builder()
      .receiptId(receiptId)
      // fields required non-null but not useful to this test
      .paymentOptionId(1L)
      .status(InstallmentStatus.REPORTED)
      .iud("IUD123")
      .amountCents(1000L)
      .remittanceInformation("REMITTANCEINFO")
      .personalDataId(1L)
      .debtorEntityType(PersonEntityType.F)
      .debtorFiscalCodeHash(new byte[0])
      .build();
    ReceiptDetailDTO receiptDetailDTO = new ReceiptDetailDTO();
    PaymentsReportingDetailDTO expectedResult = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .status(InstallmentStatus.REPORTED)
      .iud(receiptDetailDTO.getIud())
      .debtPositionTypeOrgDescription(receiptDetailDTO.getDebtPositionTypeOrgDescription())
      .paymentDateTime(receiptDetailDTO.getPaymentDateTime())
      .pspCompanyName(receiptDetailDTO.getPspCompanyName())
      .remittanceInformation(receiptDetailDTO.getRemittanceInformation())
      .debtor(receiptDetailDTO.getDebtor())
      .build();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenAnswer(a->null);

      when(
        paymentsReportingServiceMock.getPaymentsReportingDetail(organizationId,
          paymentsReportingId, accessToken))
        .thenReturn(paymentsReporting);

      when(
        installmentRetrieverServiceMock.getInstallmentFromTransferSemanticKey(
          organizationId, iuv, iur,
          String.valueOf(transferIndex), loggedUser, null, accessToken))
        .thenReturn(installmentNoPII);

      when(
        receiptRetrieverServiceMock.getReceiptDetail(organizationId, receiptId,
          loggedUser, accessToken))
        .thenReturn(receiptDetailDTO);

      when(paymentsReportingMapperMock.mapToPaymentsReportingDetailDTO(
        paymentsReporting, receiptDetailDTO))
        .thenReturn(expectedResult);

      PaymentsReportingDetailDTO result = paymentsReportingRetrieverService.getPaymentsReportingDetail(
        organizationId, iuf, paymentsReportingId, loggedUser, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenNullInstallmentWhenGetPaymentsReportingDetailThenPartialResult() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId = 1L;
    String iuf = "iuf";
    String paymentsReportingId = "PAYREP1";

    String iuv = "IUV123";
    String iur = "IUR123";
    int transferIndex = 1;
    PaymentsReporting paymentsReporting = PaymentsReporting.builder()
      .paymentsReportingId(paymentsReportingId)
      .organizationId(organizationId)
      .iuv(iuv)
      .iur(iur)
      .transferIndex(transferIndex)
      .iuf(iuf)
      // fields required non-null but not useful to this test
      .ingestionFlowFileId(1L)
      .pspIdentifier("PSPID")
      .flowDateTime(OffsetDateTime.now())
      .regulationDate(LocalDate.now())
      .regulationUniqueIdentifier("REG123")
      .senderPspCode("SENDERCODE")
      .senderPspName("SENDERNAME")
      .senderPspType("SENDERTYPE")
      .receiverOrganizationCode("RECEIVERCODE")
      .receiverOrganizationName("RECEIVERNAME")
      .receiverOrganizationType("RECEIVERTYPE")
      .totalPayments(1000L)
      .totalAmountCents(1000L)
      .amountPaidCents(1000L)
      .paymentOutcomeCode("OUTCOMECODE")
      .payDate(LocalDate.now())
      .acquiringDate(LocalDate.now())
      .build();

    PaymentsReportingDetailDTO expectedResult = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .status(InstallmentStatus.REPORTED)
      .build();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenAnswer(a->null);

      when(
        paymentsReportingServiceMock.getPaymentsReportingDetail(organizationId,
          paymentsReportingId, accessToken))
        .thenReturn(paymentsReporting);

      when(
        installmentRetrieverServiceMock.getInstallmentFromTransferSemanticKey(
          organizationId, iuv, iur,
          String.valueOf(transferIndex), loggedUser, null, accessToken))
        .thenReturn(null);

      when(paymentsReportingMapperMock.mapToPaymentsReportingDetailDTO(
        paymentsReporting, null))
        .thenReturn(expectedResult);

      PaymentsReportingDetailDTO result = paymentsReportingRetrieverService.getPaymentsReportingDetail(
        organizationId, iuf, paymentsReportingId, loggedUser, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
      Mockito.verifyNoInteractions(receiptRetrieverServiceMock);

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenNonMatchingIufWhenGetPaymentsReportingDetailThenReturnNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId = 1L;
    String iuf = "iuf";
    String paymentsReportingId = "PAYREP1";
    PaymentsReporting paymentsReporting = PaymentsReporting.builder()
      .paymentsReportingId(paymentsReportingId)
      .organizationId(organizationId)
      .iuv("iuv")
      .iur("iur")
      .transferIndex(1)
      .iuf("differentIuf")
      // fields required non-null but not useful to this test
      .ingestionFlowFileId(1L)
      .pspIdentifier("PSPID")
      .flowDateTime(OffsetDateTime.now())
      .regulationDate(LocalDate.now())
      .regulationUniqueIdentifier("REG123")
      .senderPspCode("SENDERCODE")
      .senderPspName("SENDERNAME")
      .senderPspType("SENDERTYPE")
      .receiverOrganizationCode("RECEIVERCODE")
      .receiverOrganizationName("RECEIVERNAME")
      .receiverOrganizationType("RECEIVERTYPE")
      .totalPayments(1000L)
      .totalAmountCents(1000L)
      .amountPaidCents(1000L)
      .paymentOutcomeCode("OUTCOMECODE")
      .payDate(LocalDate.now())
      .acquiringDate(LocalDate.now())
      .build();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenAnswer(a->null);

      when(
        paymentsReportingServiceMock.getPaymentsReportingDetail(organizationId,
          paymentsReportingId, accessToken))
        .thenReturn(paymentsReporting);

      PaymentsReportingDetailDTO result = paymentsReportingRetrieverService.getPaymentsReportingDetail(
        organizationId, iuf, paymentsReportingId, loggedUser, accessToken);

      assertNull(result);
      Mockito.verifyNoInteractions(installmentRetrieverServiceMock, receiptRetrieverServiceMock);

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }

  @Test
  void givenNullPaymentsReportingWhenGetPaymentsReportingDetailThenReturnNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId = 1L;
    String iuf = "iuf";
    String paymentsReportingId = "PAYREP1";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(
      AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(
          () -> AuthorizationService.validateUserForOrganizationId(organizationId,
            loggedUser))
        .thenAnswer(a->null);

      when(
        paymentsReportingServiceMock.getPaymentsReportingDetail(organizationId,
          paymentsReportingId, accessToken))
        .thenReturn(null);

      PaymentsReportingDetailDTO result = paymentsReportingRetrieverService.getPaymentsReportingDetail(
        organizationId, iuf, paymentsReportingId, loggedUser, accessToken);

      assertNull(result);

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
    }
  }
}
