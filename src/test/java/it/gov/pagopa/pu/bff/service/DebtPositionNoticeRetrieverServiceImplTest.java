package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionService;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgOperatorsService;
import it.gov.pagopa.pu.bff.connector.pagopapayments.PrintPaymentNoticeService;
import it.gov.pagopa.pu.bff.dto.FileResourceDTO;
import it.gov.pagopa.pu.bff.exception.InvalidDebtPositionException;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionNoticeRetrieverService;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionNoticeRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionNoticeRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private PrintPaymentNoticeService printPaymentNoticeServiceMock;
  @Mock
  private DebtPositionService debtPositionServiceMock;
  @Mock
  private DebtPositionTypeOrgOperatorsService debtPositionTypeOrgOperatorsServiceMock;

  private DebtPositionNoticeRetrieverService debtPositionNoticeRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    debtPositionNoticeRetrieverService = new DebtPositionNoticeRetrieverServiceImpl(
      printPaymentNoticeServiceMock, debtPositionTypeOrgOperatorsServiceMock,
      debtPositionServiceMock);
  }

  @Test
  void givenValidUserAndValidDebtPositionWhenGetNoticeThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId=1L;
    Long debtPositionId=2L;
    String nav = "nav";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setOrganizationId(organizationId);
    DebtPositionTypeOrgOperators debtPositionTypeOrgOperators = podamFactory.manufacturePojo(DebtPositionTypeOrgOperators.class);
    FileResourceDTO expectedResult = new FileResourceDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      when(debtPositionServiceMock.getDebtPosition(debtPositionId,accessToken))
        .thenReturn(debtPositionDTO);
      when(debtPositionTypeOrgOperatorsServiceMock.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionDTO.getDebtPositionTypeOrgId(),loggedUser.getMappedExternalUserId(),accessToken))
        .thenReturn(debtPositionTypeOrgOperators);
      when(
          printPaymentNoticeServiceMock.generateNotice(nav,debtPositionDTO,accessToken))
        .thenReturn(expectedResult);

      FileResourceDTO result = debtPositionNoticeRetrieverService.getNotice(organizationId, nav, debtPositionId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoMoreInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenDebtPositionNotFoundWhenGetNoticeThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId=1L;
    Long debtPositionId=2L;
    String nav = "nav";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      when(debtPositionServiceMock.getDebtPosition(debtPositionId,accessToken))
        .thenReturn(null);

      Assertions.assertThrows(NotFoundException.class,
        ()-> debtPositionNoticeRetrieverService.getNotice(organizationId, nav, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenWrongOrganizationIdWhenGetNoticeThenInvalidDebtPositionException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    Long organizationId=1L;
    Long debtPositionId=2L;
    String nav = "nav";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setOrganizationId(organizationId+1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      when(debtPositionServiceMock.getDebtPosition(debtPositionId,accessToken))
        .thenReturn(debtPositionDTO);

      Assertions.assertThrows(InvalidDebtPositionException.class,
        ()-> debtPositionNoticeRetrieverService.getNotice(organizationId, nav, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      Mockito.verifyNoInteractions(printPaymentNoticeServiceMock);
    }
  }

  @Test
  void givenNonExistingOperatorWhenGetNoticeThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("operatorExternalUserId");

    Long organizationId=1L;
    Long debtPositionId=2L;
    String nav = "nav";
    DebtPositionDTO debtPositionDTO = podamFactory.manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setOrganizationId(organizationId);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a->null);

      when(debtPositionServiceMock.getDebtPosition(debtPositionId,accessToken))
        .thenReturn(debtPositionDTO);
      when(debtPositionTypeOrgOperatorsServiceMock.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionDTO.getDebtPositionTypeOrgId(),loggedUser.getMappedExternalUserId(),accessToken))
        .thenReturn(null);

      Assertions.assertThrows(AuthorizationDeniedException.class,
        ()-> debtPositionNoticeRetrieverService.getNotice(organizationId, nav, debtPositionId, loggedUser, accessToken));

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
    String nav = "nav";

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        debtPositionNoticeRetrieverService.getNotice(organizationId, nav, debtPositionId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    Mockito.verifyNoInteractions(printPaymentNoticeServiceMock,
      debtPositionServiceMock);
  }
}



