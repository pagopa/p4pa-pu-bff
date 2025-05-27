package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionView;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionMapper;
import it.gov.pagopa.pu.bff.mapper.DebtPositionViewMapper;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionRetrieverServiceImplTest {

  @Mock
  private DebtPositionService debtPositionServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private DebtPositionViewMapper debtPositionViewMapperMock;
  @Mock
  private PrintPaymentNoticeService printPaymentNoticeServiceMock;
  @Mock
  private ZipFileService zipFileServiceMock;
  @Mock
  private DebtPositionMapper debtPositionMapperMock;

  private DebtPositionRetrieverService debtPositionRetrieverService;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionRetrieverService = new DebtPositionRetrieverServiceImpl(debtPositionServiceMock, debtPositionTypeOrgServiceMock, debtPositionViewMapperMock, debtPositionMapperMock, printPaymentNoticeServiceMock, zipFileServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      debtPositionServiceMock,
      debtPositionTypeOrgServiceMock,
      debtPositionViewMapperMock,
      debtPositionMapperMock,
      printPaymentNoticeServiceMock
    );
  }

  @Test
  void givenValidDebtPositionWhenCreateDebtPositionThenOk() {
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setDebtPositionId(null);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    DebtPositionDTO expectedResult = podamFactory.manufacturePojo(DebtPositionDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionServiceMock.createDebtPosition(debtPositionDTO, false, accessToken))
        .thenReturn(expectedResult);

      DebtPositionDTO result = debtPositionRetrieverService.createDebtPosition(debtPositionDTO, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenDebtPositionWithIdWhenCreateDebtPositionThenBadRequest() {
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setDebtPositionId(1L);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      InvalidDebtPositionException exception = assertThrows(InvalidDebtPositionException.class, () ->
        debtPositionRetrieverService.createDebtPosition(debtPositionDTO, loggedUser, accessToken));

      assertEquals("Bad Request: Debt Position ID should not be provided", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenUnauthorizedUserWhenCreateDebtPositionThenAuthorizationDeniedException() {
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setDebtPositionId(null);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      AuthorizationDeniedException exception = assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.createDebtPosition(debtPositionDTO, loggedUser, accessToken));

      assertEquals("Access denied", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(debtPositionDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenValidUserWhenGetDebtPositionViewsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    PageRequest pageRequest = PageRequest.of(0, 10);
    List<String> debtPositionOrigins = List.of(DebtPositionOrigin.ORDINARY.toString(), DebtPositionOrigin.ORDINARY_SIL.toString(), DebtPositionOrigin.SPONTANEOUS.toString());

    DebtPositionViewFiltersDTO debtPositionViewFiltersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);
    PagedModelDebtPositionView pagedModelDebtPositionView = podamFactory.manufacturePojo(
      PagedModelDebtPositionView.class);
    PagedDebtPositionView expectedResult = podamFactory.manufacturePojo(
      PagedDebtPositionView.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionServiceMock.getDebtPositionViews(debtPositionViewFiltersDTO, debtPositionOrigins, loggedUser.getMappedExternalUserId(), pageRequest,
          accessToken))
        .thenReturn(pagedModelDebtPositionView);
      Mockito.when(debtPositionViewMapperMock.mapToPagedDebtPositionView(pagedModelDebtPositionView))
        .thenReturn(expectedResult);

      PagedDebtPositionView result = debtPositionRetrieverService.getDebtPositionViews(debtPositionViewFiltersDTO, pageRequest, loggedUser,
        accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(
        debtPositionViewFiltersDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenNoFiltersWhenGetDebtPositionViewsThenThrowIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    PageRequest pageRequest = PageRequest.of(0, 10);

    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setCreationDateFrom(null);
    filtersDTO.setCreationDateTo(null);
    filtersDTO.setFiscalCode(null);
    filtersDTO.setDebtPositionTypeOrgId(null);
    filtersDTO.setStatus(null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        debtPositionRetrieverService.getDebtPositionViews(filtersDTO, pageRequest, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' creation dates must be set together", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(debtPositionServiceMock, debtPositionViewMapperMock);
  }

  @Test
  void givenOnlyCreationDateFromWhenGetDebtPositionViewsThenThrowIllegalArgumentException() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setCreationDateFrom(OffsetDateTime.now());
    filtersDTO.setCreationDateTo(null);
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenOnlyCreationDateToWhenGetDebtPositionViewsThenThrowIllegalArgumentException() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setCreationDateFrom(null);
    filtersDTO.setCreationDateTo(OffsetDateTime.now());
    assertThrowsIllegalArgument(filtersDTO);
  }

  private void assertThrowsIllegalArgument(DebtPositionViewFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    PageRequest pageRequest = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        debtPositionRetrieverService.getDebtPositionViews(filtersDTO, pageRequest, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' creation dates must be set together", exception.getMessage());
    }

    Mockito.verifyNoInteractions(debtPositionServiceMock, debtPositionViewMapperMock);
  }

  @Test
  void givenValidCreationDateRangeWhenGetDebtPositionViewsThenOk() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setCreationDateFrom(OffsetDateTime.now().minusDays(5));
    filtersDTO.setCreationDateTo(OffsetDateTime.now());
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenFiscalCodeOnlyWhenGetDebtPositionViewsThenOk() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setFiscalCode("RSSMRA80A01H501U");
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenDebtPositionTypeOrgIdOnlyWhenGetDebtPositionViewsThenOk() {
    DebtPositionViewFiltersDTO filtersDTO = new DebtPositionViewFiltersDTO();
    filtersDTO.setOrganizationId(1L);
    filtersDTO.setDebtPositionTypeOrgId(99L);
    testSingleFilterSuccess(filtersDTO);
  }

  private void testSingleFilterSuccess(DebtPositionViewFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    PageRequest pageRequest = PageRequest.of(0, 10);

    List<String> debtPositionOriginFilterList = List.of(
      DebtPositionOrigin.ORDINARY.toString(),
      DebtPositionOrigin.ORDINARY_SIL.toString(),
      DebtPositionOrigin.SPONTANEOUS.toString()
    );

    PagedModelDebtPositionView pagedModel = new PagedModelDebtPositionView();
    PagedDebtPositionView expected = new PagedDebtPositionView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionServiceMock.getDebtPositionViews(filtersDTO, debtPositionOriginFilterList, loggedUser.getMappedExternalUserId(), pageRequest, accessToken))
        .thenReturn(pagedModel);

      Mockito.when(debtPositionViewMapperMock.mapToPagedDebtPositionView(pagedModel))
        .thenReturn(expected);

      PagedDebtPositionView result = debtPositionRetrieverService.getDebtPositionViews(filtersDTO, pageRequest, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expected, result);
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionViewsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    PageRequest pageRequest = PageRequest.of(0, 10);

    DebtPositionViewFiltersDTO debtPositionViewFiltersDTO = podamFactory.manufacturePojo(
      DebtPositionViewFiltersDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionViews(debtPositionViewFiltersDTO,
          pageRequest, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(debtPositionViewFiltersDTO.getOrganizationId(), loggedUser));
    }
  }

  @Test
  void givenValidUserWhenGetDebtPositionDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(
      DebtPositionDTO.class);
    DebtPositionTypeOrg debtPositionTypeOrg = podamFactory.manufacturePojo(
      DebtPositionTypeOrg.class);
    DebtPositionDetailDTO expectedResult = podamFactory.manufacturePojo(
      DebtPositionDetailDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken))
        .thenReturn(debtPositionDTO);
      Mockito.when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrg(debtPositionDTO.getDebtPositionTypeOrgId(), accessToken))
        .thenReturn(debtPositionTypeOrg);
      Mockito.when(debtPositionMapperMock.mapToDebtPositionDetailDTO(debtPositionDTO, debtPositionTypeOrg))
        .thenReturn(expectedResult);

      DebtPositionDetailDTO result = debtPositionRetrieverService.getDebtPositionDetail(debtPositionId, organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(
        organizationId, loggedUser));
    }
  }

  @Test
  void givenValidUserAndNoDebtPositionWhenGetDebtPositionDetailThenReturnNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken))
        .thenReturn(null);

      DebtPositionDetailDTO result = debtPositionRetrieverService.getDebtPositionDetail(debtPositionId, organizationId, loggedUser, accessToken);

      assertNull(result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(
        organizationId, loggedUser));
    }
  }


  @Test
  void givenInvalidUserWhenGetDebtPositionDetailThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionDetail(debtPositionId, organizationId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidUserWhenDeleteDebtPositionThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(debtPositionServiceMock.deleteDebtPosition(debtPositionId, accessToken)).thenReturn(false);

      boolean deletedDebtPositionPhysically = debtPositionRetrieverService.deleteDebtPosition(organizationId, debtPositionId, loggedUser, accessToken);

      assertFalse(deletedDebtPositionPhysically);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }

  }

  @Test
  void givenInvalidUserWhenDeleteDebtPositionThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      assertThrows(AuthorizationDeniedException.class, () -> debtPositionRetrieverService.deleteDebtPosition(organizationId, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }

  }

  @Test
  void givenValidUserWhenGetDebtPositionNoticesZipThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;
    String iuv = "1";

    DebtPositionDTO debtPositionDTO = new DebtPositionDTO();
    PaymentOptionDTO paymentOptionDTO = new PaymentOptionDTO();
    PaymentOptionDTO paymentOptionDTO1 = new PaymentOptionDTO();
    InstallmentDTO installmentDTOUNPAID = podamFactory.manufacturePojo(InstallmentDTO.class);
    installmentDTOUNPAID.setIuv(iuv);
    installmentDTOUNPAID.setStatus(InstallmentStatus.UNPAID);
    InstallmentDTO installmentDTOUNPAYABLE = podamFactory.manufacturePojo(InstallmentDTO.class);
    installmentDTOUNPAYABLE.setIuv(iuv);
    installmentDTOUNPAYABLE.setStatus(InstallmentStatus.UNPAYABLE);
    InstallmentDTO installmentDTOPAID = podamFactory.manufacturePojo(InstallmentDTO.class);
    installmentDTOPAID.setIuv(iuv);
    installmentDTOPAID.setStatus(InstallmentStatus.PAID);
    InstallmentDTO installmentDTOWithNullStatus = podamFactory.manufacturePojo(InstallmentDTO.class);
    installmentDTOWithNullStatus.setIuv(iuv);
    installmentDTOWithNullStatus.setStatus(null);
    paymentOptionDTO.setInstallments(List.of(installmentDTOUNPAID, installmentDTOUNPAYABLE));
    paymentOptionDTO1.setInstallments(List.of(installmentDTOPAID, installmentDTOWithNullStatus));
    debtPositionDTO.setPaymentOptions(List.of(paymentOptionDTO, paymentOptionDTO1));

    ByteArrayResource expectedResult = new ByteArrayResource("PDF-DATA".getBytes());

    FileResourceDTO fileResourceDTO = new FileResourceDTO(expectedResult, "filename");

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      Mockito.when(debtPositionServiceMock.hasOperatorGrantOnDebtPosition(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(true);
      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken)).thenReturn(debtPositionDTO);
      Mockito.when(printPaymentNoticeServiceMock.generateNotice(iuv, debtPositionDTO, accessToken)).thenReturn(fileResourceDTO);

      Mockito.when(zipFileServiceMock.zipper(List.of(fileResourceDTO, fileResourceDTO))).thenReturn(expectedResult);
      Resource result = debtPositionRetrieverService.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);
      Mockito.verify(printPaymentNoticeServiceMock, Mockito.times(2)).generateNotice(iuv, debtPositionDTO, accessToken);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidUserAndDebtPositionWithNullInstallmentsWhenGetDebtPositionNoticesZipThenReturnNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    DebtPositionDTO debtPositionDTO = new DebtPositionDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      Mockito.when(debtPositionServiceMock.hasOperatorGrantOnDebtPosition(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(true);
      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken)).thenReturn(debtPositionDTO);

      Resource result = debtPositionRetrieverService.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken);

      assertNull(result);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }

  }

  @Test
  void givenValidUserAndNullDebtPositionWhenGetDebtPositionNoticesZipThenReturnNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      Mockito.when(debtPositionServiceMock.hasOperatorGrantOnDebtPosition(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(true);
      Mockito.when(debtPositionServiceMock.getDebtPosition(debtPositionId, accessToken)).thenReturn(null);

      Resource result = debtPositionRetrieverService.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken);

      assertNull(result);
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenInvalidUserWhenGetDebtPositionNoticesZipThenThrowAuthorizationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Long organizationId = 1L;
    Long debtPositionId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      authorizationServiceMockedStatic.when(() -> AuthorizationService.buildAuthorizationDeniedException(organizationId, loggedUser)).thenReturn(new AuthorizationDeniedException("Access denied on organizationId 1 to user mappedExternalUserId"));

      Mockito.when(debtPositionServiceMock.hasOperatorGrantOnDebtPosition(debtPositionId, organizationId, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(false);

      AuthorizationDeniedException ex = assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken));

      assertEquals("Access denied on organizationId 1 to user mappedExternalUserId", ex.getMessage());
      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenInvalidUserForOrganizationIdWhenGetDebtPositionRegistryThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId=1L;
    Long debtPositionId=2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionRetrieverService.getDebtPositionNoticesZip(organizationId, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenValidUserWhenValidateOperatorThenDoNothing(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId=1L;
    Long debtPositionId=2L;
    Mockito.when(debtPositionServiceMock.hasOperatorGrantOnDebtPosition(debtPositionId,organizationId,loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(true);

    debtPositionRetrieverService.validateOperator(debtPositionId, organizationId, loggedUser, accessToken);

    Mockito.verifyNoMoreInteractions(debtPositionServiceMock);

  }

  @Test
  void givenInvalidUserWhenValidateOperatorThenThrowException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId=1L;
    Long debtPositionId=2L;

    Mockito.when(debtPositionServiceMock.hasOperatorGrantOnDebtPosition(debtPositionId,organizationId, loggedUser.getMappedExternalUserId(), accessToken)).thenReturn(false);

    AuthorizationDeniedException ex = assertThrows(AuthorizationDeniedException.class, () -> debtPositionRetrieverService.validateOperator(debtPositionId, organizationId, loggedUser, accessToken));
    assertEquals("Access denied on organizationId 1 to user operatorExternalUserId", ex.getMessage());
  }
}
