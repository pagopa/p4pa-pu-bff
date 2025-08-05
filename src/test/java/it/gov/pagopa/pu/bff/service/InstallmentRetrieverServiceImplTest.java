package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class InstallmentRetrieverServiceImplTest {

  @Mock
  private InstallmentService installmentServiceMock;

  @Mock
  private InstallmentViewMapper installmentViewMapperMock;

  private InstallmentRetrieverServiceImpl installmentRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    installmentRetrieverService = new InstallmentRetrieverServiceImpl(installmentViewMapperMock, installmentServiceMock);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(installmentServiceMock, installmentViewMapperMock);
  }


  @Test
  void givenValidUserWhenGetInstallmentsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDateTime(new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(5), OffsetDateTime.now().plusDays(5)));
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelInstallmentView pagedModelInstallmentView = new PagedModelInstallmentView();
    PagedInstallmentView expectedPagedInstallmentView = new PagedInstallmentView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(installmentServiceMock.getInstallments(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelInstallmentView);

      Mockito.when(installmentViewMapperMock.mapToPagedInstallmentView(pagedModelInstallmentView))
        .thenReturn(expectedPagedInstallmentView);

      PagedInstallmentView result = installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedInstallmentView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenNoFiltersWhenGetInstallmentsThenThrowIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDateTime(new OffsetDateTimeIntervalFilter(null, null));
    filtersDTO.setIuv(null);
    filtersDTO.setFiscalCode(null);
    filtersDTO.setDebtPositionTypeOrgId(null);

    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' due dates must be set together", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(installmentServiceMock, installmentViewMapperMock);
  }

  @Test
  void givenOnlyDueDateFromWhenGetInstallmentsThenThrowIllegalArgumentException() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDateTime(new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(5), null));
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenOnlyDueDateToWhenGetInstallmentsThenThrowIllegalArgumentException() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDateTime(new OffsetDateTimeIntervalFilter(null, OffsetDateTime.now()));
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenEmptyDueDateIntervalWhenGetInstallmentsThenThrowIllegalArgumentException() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDateTime(new OffsetDateTimeIntervalFilter(null, null));
    assertThrowsIllegalArgument(filtersDTO);
  }

  private void assertThrowsIllegalArgument(InstallmentViewFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic
        .when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenAnswer(a -> null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' due dates must be set together", exception.getMessage());
    }

    Mockito.verifyNoInteractions(installmentServiceMock, installmentViewMapperMock);
  }

  @Test
  void givenValidDueDateRangeWhenGetInstallmentsThenOk() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDateTime(new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(3), OffsetDateTime.now().plusDays(3)));
    testSingleInstallmentFilterSuccess(filtersDTO);
  }

  @Test
  void givenIuvOnlyWhenGetInstallmentsThenOk() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setIuv("IUV123");
    testSingleInstallmentFilterSuccess(filtersDTO);
  }

  @Test
  void givenFiscalCodeOnlyWhenGetInstallmentsThenOk() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setFiscalCode("RSSMRA80A01H501U");
    testSingleInstallmentFilterSuccess(filtersDTO);
  }

  @Test
  void givenDebtPositionTypeOrgIdOnlyWhenGetInstallmentsThenOk() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDebtPositionTypeOrgId(99L);
    testSingleInstallmentFilterSuccess(filtersDTO);
  }

  private void testSingleInstallmentFilterSuccess(InstallmentViewFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Pageable pageable = PageRequest.of(0, 10);
    PagedModelInstallmentView pagedModel = new PagedModelInstallmentView();
    PagedInstallmentView expected = new PagedInstallmentView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic
        .when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenAnswer(a -> null);

      Mockito.when(installmentServiceMock.getInstallments(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModel);

      Mockito.when(installmentViewMapperMock.mapToPagedInstallmentView(pagedModel))
        .thenReturn(expected);

      PagedInstallmentView result = installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);
    }
  }

  @Test
  void givenInvalidUserWhenGetInstallmentsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        installmentRetrieverService.getInstallments(filtersDTO, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenValidUserWhenGetInstallmentDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long installmentId = 2L;
    InstallmentDetailDTO installmentDetailDTO = new InstallmentDetailDTO();
    installmentDetailDTO.setStatus(InstallmentStatus.PAID);
    installmentDetailDTO.setPayer(new PersonDTO());
    installmentDetailDTO.setPaymentDateTime(OffsetDateTime.now());
    installmentDetailDTO.setIud("iud");
    installmentDetailDTO.setIur("iur");
    installmentDetailDTO.setPspCompanyName("pspCompanyName");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(installmentServiceMock.getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(installmentDetailDTO);

      InstallmentDetailDTO result = installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(installmentDetailDTO, result);
      assertNotNull(result.getPayer());
      assertNotNull(result.getPaymentDateTime());
      assertNotNull(result.getIud());
      assertNotNull(result.getIur());
      assertNotNull(result.getPspCompanyName());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(installmentServiceMock).getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }

  @Test
  void givenInvalidUserWhenGetInstallmentDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long installmentId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(installmentServiceMock);
    }
  }

  @Test
  void givenInstallmentWithNonPaidOrReportedStatusWhenGetInstallmentDetailThenFieldsAreNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long installmentId = 2L;
    InstallmentDetailDTO installmentDetailDTO = new InstallmentDetailDTO();
    installmentDetailDTO.setStatus(InstallmentStatus.UNPAID);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(installmentServiceMock.getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(installmentDetailDTO);

      InstallmentDetailDTO result = installmentRetrieverService.getInstallmentDetail(organizationId, installmentId, loggedUser, accessToken);

      assertNotNull(result);
      assertNull(result.getPayer());
      assertNull(result.getPaymentDateTime());
      assertNull(result.getIud());
      assertNull(result.getIur());
      assertNull(result.getPspCompanyName());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(installmentServiceMock).getInstallmentDetail(installmentId, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }

  @Test
  void givenValidUserWhenGetInstallmentFromTransferSemanticKeyThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    String iuv = "iuv";
    String iur = "iur";
    String transferIndex = "transferIndex";
    InstallmentNoPII installmentNoPII = new InstallmentNoPII();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);
      Mockito.when(installmentServiceMock.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(installmentNoPII);

      InstallmentNoPII result = installmentRetrieverService.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(installmentNoPII, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(installmentServiceMock).getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser.getMappedExternalUserId(), accessToken);
    }
  }

  @Test
  void givenInvalidUserWhenGetInstallmentFromTransferSemanticKeyThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    String iuv = "iuv";
    String iur = "iur";
    String transferIndex = "transferIndex";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        installmentRetrieverService.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(installmentServiceMock);
    }
  }

}
