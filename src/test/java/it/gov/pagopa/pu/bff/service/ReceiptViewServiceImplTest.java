package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptFilterDTO;
import it.gov.pagopa.pu.bff.mapper.ReceiptViewMapper;
import it.gov.pagopa.pu.bff.service.receipts.ReceiptViewServiceImpl;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class ReceiptViewServiceImplTest {

  @Mock
  private AuthorizationService authorizationServiceMock;

  @Mock
  private ReceiptClient receiptClientMock;

  @Mock
  private ReceiptViewMapper receiptViewMapperMock;

  private ReceiptViewServiceImpl receiptViewService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    receiptViewService = new ReceiptViewServiceImpl(authorizationServiceMock, receiptClientMock, receiptViewMapperMock);
  }

  @Test
  void givenValidUserWhenGetReceiptsThenOk() {
    ReceiptFilterDTO filter = new ReceiptFilterDTO();
    filter.setOrganizationId(1L);
    filter.setReceiptOrigin("ORIGIN");
    filter.setIuv("IUV123");
    filter.setIur("IUR456");
    filter.setIud("IUD789");
    Pageable pageable = PageRequest.of(0, 10);

    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    PagedModelReceiptView pagedModelReceiptView = new PagedModelReceiptView();
    PagedReceiptView expectedPagedReceiptView = new PagedReceiptView();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(filter.getOrganizationId(), loggedUser);
    Mockito.when(receiptClientMock.getReceipts(
      filter,
      pageable,
      accessToken
    )).thenReturn(pagedModelReceiptView);

    Mockito.when(receiptViewMapperMock.mapToPagedReceiptView(pagedModelReceiptView))
      .thenReturn(expectedPagedReceiptView);

    PagedReceiptView result = receiptViewService.getReceipts(
      filter, pageable, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(expectedPagedReceiptView, result);

    Mockito.verify(authorizationServiceMock).validateAdminRole(filter.getOrganizationId(), loggedUser);
    Mockito.verify(receiptClientMock).getReceipts(filter, pageable, accessToken);
    Mockito.verify(receiptViewMapperMock).mapToPagedReceiptView(pagedModelReceiptView);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, receiptClientMock, receiptViewMapperMock);
  }


  @Test
  void givenInvalidUserWhenGetReceiptsThenAuthorizationDeniedException() {
    ReceiptFilterDTO filter = new ReceiptFilterDTO();
    filter.setOrganizationId(1L);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    Mockito.doThrow(new AuthorizationDeniedException("Access denied"))
      .when(authorizationServiceMock).validateAdminRole(filter.getOrganizationId(), loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      receiptViewService.getReceipts(
        filter, pageable, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateAdminRole(filter.getOrganizationId(), loggedUser);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(receiptClientMock, receiptViewMapperMock);
  }

}
