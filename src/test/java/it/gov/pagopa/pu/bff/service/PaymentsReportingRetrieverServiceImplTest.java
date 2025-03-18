package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingRow;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.dto.generated.PaymentsReportingDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingMapper;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingViewMapper;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverService;
import it.gov.pagopa.pu.bff.service.payments_reporting.PaymentsReportingRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReporting;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII.DebtorEntityTypeEnum;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII.StatusEnum;
import java.time.LocalDate;
import java.time.OffsetDateTime;
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
          regulationUniqueIdentifier, regulationDateFilter, pageable,
          accessToken))
        .thenReturn(pagedModelPaymentsReportingView);

      when(paymentsReportingViewMapperMock.mapToPagedPaymentsReporting(
        pagedModelPaymentsReportingView))
        .thenReturn(expectedPagedPaymentsReportingView);

      PagedPaymentsReportingView result = paymentsReportingRetrieverService.getPaymentsReporting(
        organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter,
        pageable, loggedUser, accessToken);

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
          iuf, regulationUniqueIdentifier, regulationDateFilter, pageable,
          loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(
        () -> AuthorizationService.validateUserForOrganizationId(organizationId,
          loggedUser));
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

    PagedModelPaymentsReporting pagedModelPaymentsReporting = new PagedModelPaymentsReporting();
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
      .status(StatusEnum.REPORTED)
      .iud("IUD123")
      .amountCents(1000L)
      .remittanceInformation("REMITTANCEINFO")
      .personalDataId(1L)
      .debtorEntityType(DebtorEntityTypeEnum.F)
      .debtorFiscalCodeHash(new byte[0])
      .build();
    ReceiptDetailDTO receiptDetailDTO = new ReceiptDetailDTO();
    PaymentsReportingDetailDTO expectedResult = PaymentsReportingDetailDTO.builder()
      .paymentsReportingId(paymentsReporting.getPaymentsReportingId())
      .iuv(paymentsReporting.getIuv())
      .iur(paymentsReporting.getIur())
      .amountPaidCents(paymentsReporting.getAmountPaidCents())
      .status(InstallmentDetailDTO.StatusEnum.REPORTED)
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
          String.valueOf(transferIndex), loggedUser, accessToken))
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
      .status(InstallmentDetailDTO.StatusEnum.REPORTED)
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
          String.valueOf(transferIndex), loggedUser, accessToken))
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
