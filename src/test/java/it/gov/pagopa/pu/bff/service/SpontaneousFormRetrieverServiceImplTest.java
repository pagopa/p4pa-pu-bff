package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private SpontaneousFormService spontaneousFormServiceMock;

  private SpontaneousFormRetrieverService spontaneousFormRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    spontaneousFormRetrieverService = new SpontaneousFormRetrieverServiceImpl(spontaneousFormServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(spontaneousFormServiceMock);
  }

  @Test
  void givenValidUserWhenGetSpontaneousFormsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;

    List<SpontaneousForm> expectedResult = podamFactory.manufacturePojo(List.class,SpontaneousForm.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.findAllByOrganizationId(organizationId,accessToken))
        .thenReturn(expectedResult);

      List<SpontaneousForm> result = spontaneousFormRetrieverService.getSpontaneousForms(organizationId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenSpontaneousFormNotFoundWhenGetSpontaneousFormAndValidateThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long debtPositionTypeId = 10L;
    long spontaneousFormId = 999L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setSpontaneousFormId(spontaneousFormId);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic =
           Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId,accessToken))
        .thenReturn(null);

      ResourceNotFoundException ex = Assertions.assertThrows(
        ResourceNotFoundException.class,
        () -> spontaneousFormRetrieverService.getSpontaneousFormAndValidate(
          spontaneousFormId, debtPositionTypeOrg, accessToken
        )
      );

      Assertions.assertEquals(
        "SpontaneousForm with id 999 not found",
        ex.getMessage()
      );
    }
  }

  @Test
  void givenSpontaneousFormMismatchOrganizationIdWhenGetSpontaneousFormAndValidateThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long debtPositionTypeId = 10L;
    long spontaneousFormId = 999L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setOrganizationId(organizationId);
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setSpontaneousFormId(spontaneousFormId);

    SpontaneousForm spontaneousForm = new SpontaneousForm();
    spontaneousForm.setOrganizationId(3L);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);


    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic =
           Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId,accessToken))
        .thenReturn(spontaneousForm);

      ConflictException ex = Assertions.assertThrows(
        ConflictException.class,
        () -> spontaneousFormRetrieverService.getSpontaneousFormAndValidate(
          spontaneousFormId, debtPositionTypeOrg, accessToken
        )
      );

      Assertions.assertEquals(
        "OrganizationId 1 of DebtPositionTypeOrg does not match OrganizationId 3 of SpontaneousForm 999",
        ex.getMessage()
      );

    }
  }
}
