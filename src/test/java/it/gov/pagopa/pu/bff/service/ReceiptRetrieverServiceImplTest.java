package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.ReceiptService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptDetailDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.ReceiptDetailDTOMapper;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptFileService;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptOriginType;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReceiptRetrieverServiceImplTest {

  @Mock
  private ReceiptService receiptServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;
  @Mock
  private ReceiptFileService receiptFileServiceMock;

  @Mock
  private ReceiptViewMapper receiptViewMapperMock;
  @Mock
  private ReceiptDetailDTOMapper receiptDetailDTOMapperMock;

  private ReceiptRetrieverServiceImpl receiptViewService;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    receiptViewService = new ReceiptRetrieverServiceImpl(receiptServiceMock, organizationServiceMock, receiptFileServiceMock, receiptViewMapperMock, receiptDetailDTOMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
            receiptServiceMock,organizationServiceMock, receiptFileServiceMock,receiptViewMapperMock,receiptDetailDTOMapperMock
    );
  }

  @Test
  void givenValidUserWhenGetReceiptsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    ReceiptOriginType receiptOrigin = ReceiptOriginType.RECEIPT_PAGOPA;
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, null, "IUV123", "IUR456", "IUD789", null, paymentDateTimeFilter);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelReceiptView pagedModelReceiptView = new PagedModelReceiptView();
    PagedReceiptView expectedPagedReceiptView = new PagedReceiptView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(receiptServiceMock.getReceipts(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelReceiptView);

      Mockito.when(receiptViewMapperMock.mapToPagedReceiptView(pagedModelReceiptView))
        .thenReturn(expectedPagedReceiptView);

      PagedReceiptView result = receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedReceiptView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
      Mockito.verify(receiptServiceMock).getReceipts(filtersDTO, pageable, accessToken);
      Mockito.verify(receiptViewMapperMock).mapToPagedReceiptView(pagedModelReceiptView);
      Mockito.verifyNoMoreInteractions(receiptServiceMock, receiptViewMapperMock);
    }
  }

  @Test
  void givenNoFiltersWhenGetReceiptsThenThrowIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(
      1L, null, null, null, null, null, null, new OffsetDateTimeIntervalFilter(null, null)
    );
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' payment dates must be set together", exception.getMessage());

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(receiptServiceMock, receiptViewMapperMock);
  }

  @Test
  void givenInvalidUserWhenGetReceiptsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    ReceiptOriginType receiptOrigin = ReceiptOriginType.RECEIPT_PAGOPA;
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, null, "IUV123", "IUR456", "IUD789", null, paymentDateTimeFilter);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(receiptServiceMock, receiptViewMapperMock);
  }

  @Test
  void givenOnlyPaymentDateTimeFromWhenGetReceiptsThenThrowIllegalArgumentException() {
    OffsetDateTimeIntervalFilter paymentDateTime = new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(2), null);
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, null, null, null, paymentDateTime);
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenOnlyPaymentDateTimeToWhenGetReceiptsThenThrowIllegalArgumentException() {
    OffsetDateTimeIntervalFilter paymentDateTime = new OffsetDateTimeIntervalFilter(null, OffsetDateTime.now());
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, null, null, null, paymentDateTime);
    assertThrowsIllegalArgument(filtersDTO);
  }

  @Test
  void givenEmptyPaymentDateTimeIntervalWhenGetReceiptsThenThrowIllegalArgumentException() {
    OffsetDateTimeIntervalFilter paymentDateTime = new OffsetDateTimeIntervalFilter(null, null);
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, null, null, null, paymentDateTime);
    assertThrowsIllegalArgument(filtersDTO);
  }

  private void assertThrowsIllegalArgument(ReceiptViewFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one of the research fields must be provided, and both 'from' and 'to' payment dates must be set together", exception.getMessage());
    }

    Mockito.verifyNoInteractions(receiptServiceMock, receiptViewMapperMock);
  }

  @Test
  void givenValidPaymentDateTimeRangeWhenGetReceiptsThenOk() {
    OffsetDateTimeIntervalFilter paymentDateTime = new OffsetDateTimeIntervalFilter(OffsetDateTime.now().minusDays(2), OffsetDateTime.now());
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, null, null, null, paymentDateTime);
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenDebtPositionTypeOrgIdOnlyWhenGetReceiptsThenOk() {
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, null, null, 99L, null);
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenIudOnlyWhenGetReceiptsThenOk() {
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, null, "IUD789", null, null);
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenIurOnlyWhenGetReceiptsThenOk() {
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, null, "IUR456", null, null, null);
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenIuvOnlyWhenGetReceiptsThenOk() {
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, null, null, "IUV123", null, null, null, null);
    testSingleFilterSuccess(filtersDTO);
  }

  @Test
  void givenReceiptOriginOnlyWhenGetReceiptsThenOk() {
    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, ReceiptOriginType.RECEIPT_PAGOPA, null, null, null, null, null, null);
    testSingleFilterSuccess(filtersDTO);
  }

  private void testSingleFilterSuccess(ReceiptViewFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelReceiptView pagedModelReceiptView = new PagedModelReceiptView();
    PagedReceiptView expectedPagedReceiptView = new PagedReceiptView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(filtersDTO.getOrganizationId(), loggedUser)).thenAnswer(a -> null);

      Mockito.when(receiptServiceMock.getReceipts(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelReceiptView);

      Mockito.when(receiptViewMapperMock.mapToPagedReceiptView(pagedModelReceiptView))
        .thenReturn(expectedPagedReceiptView);

      PagedReceiptView result = receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedReceiptView, result);
    }
  }

  @Test
  void givenValidUserWhenGetReceiptDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long receiptId = 2L;
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO = new it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO();
    ReceiptDetailDTO expectedResult = new ReceiptDetailDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(receiptServiceMock.getReceiptDetail(receiptId, loggedUser.getMappedExternalUserId(), accessToken))
        .thenReturn(receiptDetailDTO);
      Mockito.when(receiptDetailDTOMapperMock.mapToReceiptDetailDTO(receiptDetailDTO))
        .thenReturn(expectedResult);

      ReceiptDetailDTO result = receiptViewService.getReceiptDetail(organizationId, receiptId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verify(receiptServiceMock).getReceiptDetail(receiptId,
        loggedUser.getMappedExternalUserId(), accessToken);
      Mockito.verify(receiptDetailDTOMapperMock).mapToReceiptDetailDTO(receiptDetailDTO);
    }
  }

  @Test
  void givenInvalidUserWhenGetReceiptDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long receiptId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        receiptViewService.getReceiptDetail(organizationId, receiptId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(receiptServiceMock, receiptDetailDTOMapperMock);
    }
  }

  @Test
  void givenValidUserWhenGetReceiptPdfThenOk(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long receiptId = 2L;
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO.class);
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    byte[] pdfBytes = "PDF-DATA".getBytes();
    FileResourceDTO expectedResult = new FileResourceDTO(
            new ByteArrayResource(pdfBytes),
            "RECEIPT_"+organization.getOrgFiscalCode()+"_"+receiptId+".pdf"
    );

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(receiptServiceMock.getReceiptDetail(receiptId, loggedUser.getMappedExternalUserId(), accessToken))
              .thenReturn(receiptDetailDTO);
      Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(organization);
      Mockito.when(receiptFileServiceMock.generateReceiptPdf(receiptDetailDTO,organization)).thenReturn(pdfBytes);

      FileResourceDTO result = receiptViewService.getReceiptPdf(organizationId, receiptId, loggedUser, accessToken);

      assertNotNull(result);
      assertEquals(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNoOrganizationWhenGetReceiptPdfThenResourceNotFoundException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long receiptId = 2L;
    it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetailDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(receiptServiceMock.getReceiptDetail(receiptId, loggedUser.getMappedExternalUserId(), accessToken))
              .thenReturn(receiptDetailDTO);
      Mockito.when(organizationServiceMock.getOrganizationByOrganizationId(organizationId,accessToken)).thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,()-> receiptViewService.getReceiptPdf(organizationId, receiptId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(receiptFileServiceMock);
    }
  }

  @Test
  void givenNoReceiptWhenGetReceiptPdfThenResourceNotFoundException(){
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long receiptId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(receiptServiceMock.getReceiptDetail(receiptId, loggedUser.getMappedExternalUserId(), accessToken))
              .thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,()-> receiptViewService.getReceiptPdf(organizationId, receiptId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(organizationServiceMock, receiptFileServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetReceiptPdfThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    Long organizationId = 1L;
    Long receiptId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
              .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
              receiptViewService.getReceiptPdf(organizationId, receiptId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(receiptServiceMock,organizationServiceMock, receiptFileServiceMock);
    }
  }
}



