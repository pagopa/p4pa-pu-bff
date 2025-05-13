package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.service.pagopapayments.PrintPaymentNoticeRetrieverService;
import it.gov.pagopa.pu.bff.service.pagopapayments.PrintPaymentNoticeRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.pagopapayments.dto.generated.DebtPositionDTO;
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

  private PrintPaymentNoticeRetrieverService printPaymentNoticeRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    printPaymentNoticeRetrieverService = new PrintPaymentNoticeRetrieverServiceImpl(
      printPaymentNoticeServiceMock);
  }

  @Test
  void givenValidUserAndValidDebtPositionWhenGenerateNoticeThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId=1L;
    String iuv = "iuv";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setOrganizationId(organizationId);
    FileResourceDTO expectedResult = new FileResourceDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      Mockito.when(
          printPaymentNoticeServiceMock.generateNotice(organizationId,iuv,debtPositionDTO,accessToken))
        .thenReturn(expectedResult);

      FileResourceDTO result = printPaymentNoticeRetrieverService.generateNotice(organizationId, iuv, debtPositionDTO, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoMoreInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenInvalidDebtPositionWhenGenerateNoticeThenInvalidDebtPositionException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId=1L;
    String iuv = "iuv";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      Assertions.assertThrows(InvalidDebtPositionException.class,
        ()->printPaymentNoticeRetrieverService.generateNotice(organizationId, iuv, debtPositionDTO, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetReceiptsThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");


    Long organizationId=1L;
    String iuv = "iuv";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        printPaymentNoticeRetrieverService.generateNotice(organizationId, iuv, debtPositionDTO, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    Mockito.verifyNoInteractions(printPaymentNoticeServiceMock);
  }
}



