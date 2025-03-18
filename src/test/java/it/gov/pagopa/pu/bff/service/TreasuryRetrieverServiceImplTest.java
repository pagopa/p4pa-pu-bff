package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.TreasuryService;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuryView;
import it.gov.pagopa.pu.bff.mapper.TreasuryViewMapper;
import it.gov.pagopa.pu.bff.service.treasury.TreasuryRetrieverServiceImpl;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

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
    LocalDate billDate = LocalDate.now().minusDays(10);
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDate = LocalDate.now().minusDays(5);
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDate, provisionalCode, billCode, pspLastName, regionValueDate, documentCode);

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
  void givenInvalidUserWhenGetTreasuriesThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String iuv = "IUV123";
    String iuf = "IUF123";
    long billAmountCents = 1000L;
    LocalDate billDate = LocalDate.now().minusDays(10);
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDate = LocalDate.now().minusDays(5);
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10);

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(organizationId, iuv, iuf, billAmountCents, billDate, provisionalCode, billCode, pspLastName, regionValueDate, documentCode);

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
