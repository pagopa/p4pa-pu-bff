package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionDetailDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionRetrieverService;
import it.gov.pagopa.pu.bff.service.pagopapayments.PrintPaymentNoticeRetrieverService;
import it.gov.pagopa.pu.bff.service.pagopapayments.PrintPaymentNoticeRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authorization.AuthorizationDeniedException;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PrintPaymentNoticeRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private PrintPaymentNoticeService printPaymentNoticeServiceMock;
  @Mock
  private DebtPositionRetrieverService debtPositionRetrieverServiceMock;

  private PrintPaymentNoticeRetrieverService printPaymentNoticeRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    printPaymentNoticeRetrieverService = new PrintPaymentNoticeRetrieverServiceImpl(
      printPaymentNoticeServiceMock, debtPositionRetrieverServiceMock);
  }

  @Test
  void givenValidUserAndValidDebtPositionWhenGenerateNoticeThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId=1L;
    Long debtPositionId=2L;
    String iuv = "iuv";
    DebtPositionDetailDTO debtPositionDetail = podamFactory.manufacturePojo(DebtPositionDetailDTO.class);
    FileResourceDTO expectedResult = new FileResourceDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionDetail(debtPositionId,organizationId,loggedUser,accessToken))
        .thenReturn(debtPositionDetail);
      Mockito.when(
          printPaymentNoticeServiceMock.generateNotice(iuv,debtPositionDetail,accessToken))
        .thenReturn(expectedResult);

      FileResourceDTO result = printPaymentNoticeRetrieverService.generateNotice(organizationId, iuv, debtPositionId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoMoreInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenDebtPositionNotFoundWhenGenerateNoticeThenInvalidDebtPositionException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId=1L;
    Long debtPositionId=2L;
    String iuv = "iuv";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      Mockito.when(debtPositionRetrieverServiceMock.getDebtPositionDetail(debtPositionId,organizationId,loggedUser,accessToken))
        .thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,
        ()->printPaymentNoticeRetrieverService.generateNotice(organizationId, iuv, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetReceiptsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");


    Long organizationId=1L;
    Long debtPositionId=2L;
    String iuv = "iuv";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        printPaymentNoticeRetrieverService.generateNotice(organizationId, iuv, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    Mockito.verifyNoInteractions(printPaymentNoticeServiceMock,debtPositionRetrieverServiceMock);
  }
}



