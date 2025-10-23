package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.TreasuryService;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.mapper.TreasuryViewMapper;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverServiceImpl;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import it.gov.pagopa.pu.classification.dto.generated.TreasuryOrigin;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreasuryRetrieverServiceImplTest {

  @Mock
  private TreasuryService treasuryServiceMock;

  @Mock
  private TreasuryViewMapper treasuryViewMapperMock;

  private TreasuryRetrieverServiceImpl treasuryRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    treasuryRetrieverService = new TreasuryRetrieverServiceImpl(treasuryServiceMock, treasuryViewMapperMock);
  }

  @Test
  void givenValidUserWhenGetTreasuriesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDateFrom = LocalDate.now().minusDays(10);
    LocalDate billDateTo = LocalDate.now();
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDateFrom = LocalDate.now().minusDays(5);
    LocalDate regionValueDateTo = LocalDate.now();
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDateFilter, provisionalCode, null, billCode, null, pspLastName, regionValueDateFilter, documentCode, null);

    PagedModelTreasuryView pagedModelTreasuryView = new PagedModelTreasuryView();
    PagedTreasuryView expectedPagedTreasuryView = new PagedTreasuryView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(treasuryServiceMock.getTreasuries(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelTreasuryView);

      Mockito.when(treasuryViewMapperMock.mapToPagedTreasury(pagedModelTreasuryView))
        .thenReturn(expectedPagedTreasuryView);

      PagedTreasuryView result = treasuryRetrieverService.getTreasuries(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedTreasuryView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
      Mockito.verify(treasuryServiceMock).getTreasuries(filtersDTO, pageable, accessToken);
      Mockito.verify(treasuryViewMapperMock).mapToPagedTreasury(pagedModelTreasuryView);
      Mockito.verifyNoMoreInteractions(treasuryServiceMock, treasuryViewMapperMock);
    }
  }

  @Test
  void givenValidUserAndNullDateFiltersWhenGetTreasuriesThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, null, provisionalCode, null, billCode, null, pspLastName, null, documentCode, null);

    PagedModelTreasuryView pagedModelTreasuryView = new PagedModelTreasuryView();
    PagedTreasuryView expectedPagedTreasuryView = new PagedTreasuryView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(treasuryServiceMock.getTreasuries(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelTreasuryView);

      Mockito.when(treasuryViewMapperMock.mapToPagedTreasury(pagedModelTreasuryView))
        .thenReturn(expectedPagedTreasuryView);

      PagedTreasuryView result = treasuryRetrieverService.getTreasuries(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedTreasuryView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
      Mockito.verify(treasuryServiceMock).getTreasuries(filtersDTO, pageable, accessToken);
      Mockito.verify(treasuryViewMapperMock).mapToPagedTreasury(pagedModelTreasuryView);
      Mockito.verifyNoMoreInteractions(treasuryServiceMock, treasuryViewMapperMock);
    }
  }

  @Test
  void whenBillDateFilterIsInvalidThenThrowException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDateFrom = LocalDate.now().minusDays(10);
    LocalDate billDateTo = null; // Invalid case
    String provisionalCode = "PROV123";
    String provisionalAe = "PROVAE123";
    String billCode = "BILL123";
    String billYear = "2025";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDateFrom = LocalDate.now().minusDays(5);
    LocalDate regionValueDateTo = LocalDate.now();
    String documentCode = "DOC123";
    String documentYear = "2025";
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(
      organizationId, iuv, iuf, billAmountCents, billDateFilter, provisionalCode, provisionalAe, billCode, billYear, pspLastName, regionValueDateFilter, documentCode, documentYear
    );

    Exception exception = assertThrows(IllegalArgumentException.class, () -> treasuryRetrieverService.getTreasuries(filtersDTO, pageable, loggedUser, accessToken));

    String expectedMessage = "Both billDateFrom and billDateTo must be set or both must be null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void whenBillDateFilterIsInvalidThenThrowExceptionAndNoOtherInteractions() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDateFrom = LocalDate.now().minusDays(10);
    LocalDate billDateTo = null;
    String provisionalCode = "PROV123";
    String provisionalAe = "PROVAE123";
    String billCode = "BILL123";
    String billYear = "2025";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDateFrom = LocalDate.now().minusDays(5);
    LocalDate regionValueDateTo = LocalDate.now();
    String documentCode = "DOC123";
    String documentYear = "2025";
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(
      organizationId, iuv, iuf, billAmountCents, billDateFilter, provisionalCode, provisionalAe, billCode, billYear, pspLastName, regionValueDateFilter, documentCode, documentYear
    );

    Exception exception = assertThrows(IllegalArgumentException.class, () -> treasuryRetrieverService.getTreasuries(filtersDTO, pageable, loggedUser, accessToken));

    String expectedMessage = "Both billDateFrom and billDateTo must be set or both must be null";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    Mockito.verifyNoInteractions(treasuryServiceMock, treasuryViewMapperMock);
  }

  @Test
  void givenInvalidUserWhenGetTreasuriesThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDateFrom = LocalDate.now().minusDays(10);
    LocalDate billDateTo = LocalDate.now();
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDateFrom = LocalDate.now().minusDays(5);
    LocalDate regionValueDateTo = LocalDate.now();
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10);

    LocalDateIntervalFilter billDateFilter = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDateFilter = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDateFilter, provisionalCode, null, billCode, null, pspLastName, regionValueDateFilter, documentCode, null);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        treasuryRetrieverService.getTreasuries(filtersDTO, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(treasuryServiceMock, treasuryViewMapperMock);
  }

  @Test
  void givenValidUserWhenGetTreasuryDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String treasuryId = "TREASURY123";
    Treasury expectedTreasury = Treasury.builder()
      .treasuryId(treasuryId)
      .organizationId(organizationId)
      .billYear("2025")
      .billCode("BILL123")
      .ingestionFlowFileId(100L)
      .billAmountCents(1000L)
      .billDate(LocalDate.now().minusDays(10))
      .pspLastName("PSPLastName")
      .orgBtCode("orgBtCode")
      .orgIstatCode("orgIstatCode")
      .treasuryOrigin(TreasuryOrigin.TREASURY_OPI)
      .build();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(treasuryServiceMock.getTreasuryDetail(Mockito.eq(organizationId), Mockito.eq(treasuryId), Mockito.anyString()))
        .thenReturn(expectedTreasury);

      Treasury result = treasuryRetrieverService.getTreasuryDetail(organizationId, treasuryId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedTreasury, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(treasuryServiceMock).getTreasuryDetail(Mockito.eq(organizationId), Mockito.eq(treasuryId), Mockito.anyString());
      Mockito.verifyNoMoreInteractions(treasuryServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetTreasuryDetailThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String treasuryId = "TREASURY123";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        treasuryRetrieverService.getTreasuryDetail(organizationId, treasuryId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    Mockito.verifyNoInteractions(treasuryServiceMock);
  }

}
