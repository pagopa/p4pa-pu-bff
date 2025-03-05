package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReporting;
import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingMapper;
import it.gov.pagopa.pu.bff.mapper.PaymentsReportingViewMapper;
import it.gov.pagopa.pu.bff.service.payments_reporting.PaymentsReportingRetrieverServiceImpl;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReporting;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingRetrieverServiceImplTest {

  @Mock
  private PaymentsReportingService paymentsReportingServiceMock;

  @Mock
  private PaymentsReportingViewMapper paymentsReportingViewMapperMock;
  @Mock
  private PaymentsReportingMapper paymentsReportingMapperMock;

  private PaymentsReportingRetrieverServiceImpl paymentsReportingRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    paymentsReportingRetrieverService = new PaymentsReportingRetrieverServiceImpl(paymentsReportingServiceMock, paymentsReportingViewMapperMock,
      paymentsReportingMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(paymentsReportingServiceMock, paymentsReportingViewMapperMock,
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
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelPaymentsReportingView pagedModelPaymentsReportingView = new PagedModelPaymentsReportingView();
    PagedPaymentsReportingView expectedPagedPaymentsReportingView = new PagedPaymentsReportingView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);

      when(paymentsReportingServiceMock.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken))
        .thenReturn(pagedModelPaymentsReportingView);

      when(paymentsReportingViewMapperMock.mapToPagedPaymentsReporting(pagedModelPaymentsReportingView))
        .thenReturn(expectedPagedPaymentsReportingView);

      PagedPaymentsReportingView result = paymentsReportingRetrieverService.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedPaymentsReportingView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
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
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        paymentsReportingRetrieverService.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidUserWhenGetPaymentsReportingDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "iuv";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelPaymentsReporting pagedModelPaymentsReporting = new PagedModelPaymentsReporting();
    PagedPaymentsReporting expectedResult = new PagedPaymentsReporting();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenReturn(true);

      when(paymentsReportingServiceMock.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, accessToken))
        .thenReturn(pagedModelPaymentsReporting);

      when(paymentsReportingMapperMock.mapToPagedPaymentsReporting(pagedModelPaymentsReporting))
        .thenReturn(expectedResult);

      PagedPaymentsReporting result = paymentsReportingRetrieverService.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetPaymentsReportingDetailThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuf = "IUF123";
    String iuv = "iuv";
    LocalDate payDateFrom = LocalDate.now().minusDays(10);
    LocalDate payDateTo = LocalDate.now();
    LocalDateIntervalFilter payDateFilter = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        paymentsReportingRetrieverService.getPaymentsReportingDetail(organizationId, iuf, iuv, payDateFilter, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(organizationId, loggedUser));
    }
  }
}
