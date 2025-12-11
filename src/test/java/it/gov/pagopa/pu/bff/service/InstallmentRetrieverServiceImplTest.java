package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.InstallmentService;
import it.gov.pagopa.pu.bff.dto.InstallmentViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedInstallmentView;
import it.gov.pagopa.pu.bff.mapper.InstallmentViewMapper;
import it.gov.pagopa.pu.bff.service.installment.InstallmentRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentDetailDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentStatus;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelInstallmentView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;

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
    filtersDTO.setDueDate(new LocalDateIntervalFilter(LocalDate.now().minusDays(5), LocalDate.now().plusDays(5)));
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

  @ParameterizedTest
  @MethodSource("invalidFiltersProvider")
  void testInvalidInstallmentViewFilters(InstallmentViewFiltersDTO filtersDTO) {
    assertThrowsIllegalArgument(filtersDTO);
  }

  static Stream<InstallmentViewFiltersDTO> invalidFiltersProvider() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDate(new LocalDateIntervalFilter(null, null));
    filtersDTO.setIuv(null);
    filtersDTO.setFiscalCode(null);
    filtersDTO.setDebtPositionTypeOrgId(null);
    filtersDTO.setStatus(null);

    LocalDateIntervalFilter onlyDateFrom = new LocalDateIntervalFilter(LocalDate.now().minusDays(5), null);
    LocalDateIntervalFilter onlyDateTo = new LocalDateIntervalFilter(null, LocalDate.now());


    return Stream.of(
      filtersDTO,
      filtersDTO.toBuilder().dueDate(onlyDateFrom).build(),
      filtersDTO.toBuilder().dueDate(onlyDateTo).build(),
      filtersDTO.toBuilder().iuv("   ").build(),
      filtersDTO.toBuilder().iud("   ").build(),
      filtersDTO.toBuilder().fiscalCode("   ").build()
    );
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

  @ParameterizedTest
  @MethodSource("validFiltersProvider")
  void testValidInstallmentViewFilters(InstallmentViewFiltersDTO filtersDTO) {
    testSingleInstallmentFilterSuccess(filtersDTO);
  }

  static Stream<InstallmentViewFiltersDTO> validFiltersProvider() {
    InstallmentViewFiltersDTO filtersDTO = new InstallmentViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDueDate(new LocalDateIntervalFilter(null, null));
    filtersDTO.setIuv(null);
    filtersDTO.setFiscalCode(null);
    filtersDTO.setDebtPositionTypeOrgId(null);
    filtersDTO.setStatus(null);

    return Stream.of(
      filtersDTO.toBuilder().dueDate(new LocalDateIntervalFilter(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3))).build(),
      filtersDTO.toBuilder().iuv("IUV123").build(),
      filtersDTO.toBuilder().iud("IUD123").build(),
      filtersDTO.toBuilder().fiscalCode("RSSMRA80A01H501U").build(),
      filtersDTO.toBuilder().debtPositionTypeOrgId(99L).build(),
      filtersDTO.toBuilder().status(InstallmentStatus.PAID).build()
    );
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
      Mockito.when(installmentServiceMock.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser.getMappedExternalUserId(), null, accessToken))
        .thenReturn(installmentNoPII);

      InstallmentNoPII result = installmentRetrieverService.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser, null, accessToken);

      assertNotNull(result);
      assertSame(installmentNoPII, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(installmentServiceMock).getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser.getMappedExternalUserId(), null, accessToken);
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
        installmentRetrieverService.getInstallmentFromTransferSemanticKey(organizationId, iuv, iur, transferIndex, loggedUser, null, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(installmentServiceMock);
    }
  }

}
