package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.ReceiptService;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.ReceiptViewFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.receipt.ReceiptRetrieverServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class ReceiptRetrieverServiceImplTest {

  @Mock
  private ReceiptService receiptServiceMock;

  @Mock
  private ReceiptViewMapper receiptViewMapperMock;

  private ReceiptRetrieverServiceImpl receiptViewService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    receiptViewService = new ReceiptRetrieverServiceImpl(receiptServiceMock, receiptViewMapperMock);
  }

  @Test
  void givenValidUserWhenGetReceiptsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, null, "IUV123", "IUR456", "IUD789", null, paymentDateTimeFilter);
    Pageable pageable = PageRequest.of(0, 10);

    PagedModelReceiptView pagedModelReceiptView = new PagedModelReceiptView();
    PagedReceiptView expectedPagedReceiptView = new PagedReceiptView();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenReturn(true);

      Mockito.when(receiptServiceMock.getReceipts(filtersDTO, pageable, accessToken))
        .thenReturn(pagedModelReceiptView);

      Mockito.when(receiptViewMapperMock.mapToPagedReceiptView(pagedModelReceiptView))
        .thenReturn(expectedPagedReceiptView);

      PagedReceiptView result = receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedPagedReceiptView, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
      Mockito.verify(receiptServiceMock).getReceipts(filtersDTO, pageable, accessToken);
      Mockito.verify(receiptViewMapperMock).mapToPagedReceiptView(pagedModelReceiptView);
      Mockito.verifyNoMoreInteractions(receiptServiceMock, receiptViewMapperMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetReceiptsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    ReceiptView.ReceiptOriginEnum receiptOrigin = ReceiptView.ReceiptOriginEnum.RECEIPT_PAGOPA;
    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter paymentDateTimeFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    ReceiptViewFiltersDTO filtersDTO = new ReceiptViewFiltersDTO(1L, receiptOrigin, null, "IUV123", "IUR456", "IUD789", null, paymentDateTimeFilter);
    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        receiptViewService.getReceipts(filtersDTO, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.isUserEnabledToOrganizationId(filtersDTO.getOrganizationId(), loggedUser));
    }
    Mockito.verifyNoInteractions(receiptServiceMock, receiptViewMapperMock);
  }

}



