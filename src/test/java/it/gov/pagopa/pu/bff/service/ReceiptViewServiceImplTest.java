package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.client.ReceiptClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
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
    long organizationId = 1L;
    String receiptOrigin = "ORIGIN";
    String iuv = "IUV123";
    String iur = "IUR456";
    String iud = "IUD789";
    Pageable pageable = PageRequest.of(0, 10);
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    PagedModelReceiptView pagedModelReceiptView = new PagedModelReceiptView();
    PagedReceiptView expectedPagedReceiptView = new PagedReceiptView();

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.when(receiptClientMock.getReceipts(
      Mockito.eq(organizationId),
      Mockito.eq(receiptOrigin),
      Mockito.isNull(),
      Mockito.eq(iuv),
      Mockito.eq(iur),
      Mockito.eq(iud),
      Mockito.isNull(),
      Mockito.isNull(),
      Mockito.isNull(),
      Mockito.eq(pageable),
      Mockito.eq(accessToken)
    )).thenReturn(pagedModelReceiptView);

    Mockito.when(receiptViewMapperMock.mapToPagedReceiptView(pagedModelReceiptView))
      .thenReturn(expectedPagedReceiptView);

    PagedReceiptView result = receiptViewService.getReceipts(
      organizationId, receiptOrigin, null, iuv, iur, iud,
      null, null, null, pageable, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(expectedPagedReceiptView, result);

    Mockito.verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.verify(receiptClientMock).getReceipts(
      organizationId, receiptOrigin, null, iuv, iur, iud, null, null, null, pageable, accessToken);
    Mockito.verify(receiptViewMapperMock).mapToPagedReceiptView(pagedModelReceiptView);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock, receiptClientMock, receiptViewMapperMock);
  }

  @Test
  void givenInvalidUserWhenGetReceiptsThenAuthorizationDeniedException() {
    long organizationId = 1L;
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);

    Mockito.doThrow(new AuthorizationDeniedException("Access denied"))
      .when(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);

    Assertions.assertThrows(AuthorizationDeniedException.class, () ->
      receiptViewService.getReceipts(
        organizationId, "ORIGIN", null, "IUV123", "IUR456", "IUD789", null, null, null, pageable, loggedUser, accessToken));

    Mockito.verify(authorizationServiceMock).validateAdminRole(organizationId, loggedUser);
    Mockito.verifyNoMoreInteractions(authorizationServiceMock);
    Mockito.verifyNoInteractions(receiptClientMock, receiptViewMapperMock);
  }

}
