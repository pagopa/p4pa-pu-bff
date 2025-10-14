package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.exception.ConflictException;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedSpontaneousFormMapper;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import jakarta.validation.ValidationException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private SpontaneousFormService spontaneousFormServiceMock;
  @Mock
  private PagedSpontaneousFormMapper pagedSpontaneousFormMapperMock;
  @Mock
  private AuthorizationService authorizationServiceMock;

  private SpontaneousFormRetrieverService spontaneousFormRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    spontaneousFormRetrieverService = new SpontaneousFormRetrieverServiceImpl(spontaneousFormServiceMock, pagedSpontaneousFormMapperMock, authorizationServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(spontaneousFormServiceMock, pagedSpontaneousFormMapperMock, authorizationServiceMock);
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
  void givenValidSpontaneousFormWhenGetSpontaneousFormAndValidateThenReturnSpontaneousForm() {
    long organizationId = 1L;
    long debtPositionTypeId = 10L;
    long spontaneousFormId = 999L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setOrganizationId(organizationId);
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setSpontaneousFormId(spontaneousFormId);

    SpontaneousForm spontaneousForm = new SpontaneousForm();
    spontaneousForm.setOrganizationId(organizationId);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);

    Mockito.when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
      .thenReturn(spontaneousForm);

    SpontaneousForm result = spontaneousFormRetrieverService
      .getSpontaneousFormAndValidate(spontaneousFormId, debtPositionTypeOrg, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertSame(spontaneousForm, result);

    Mockito.verify(spontaneousFormServiceMock).getSpontaneousForm(spontaneousFormId, accessToken);
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
        "OrganizationId 1 does not match OrganizationId 3 of SpontaneousForm 999",
        ex.getMessage()
      );

    }
  }

  @Test
  void givenValidUserWhenGetPagedSpontaneousFormsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    String code = "code";
    Pageable pageable = PageRequest.ofSize(10);

    PagedModelSpontaneousForm pagedModelSpontaneousForm = podamFactory.manufacturePojo(PagedModelSpontaneousForm.class);
    PagedSpontaneousForm expectedResult = podamFactory.manufacturePojo(PagedSpontaneousForm.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken))
          .thenReturn(pagedModelSpontaneousForm);
      Mockito.when(pagedSpontaneousFormMapperMock.map(pagedModelSpontaneousForm))
          .thenReturn(expectedResult);

      PagedSpontaneousForm result = spontaneousFormRetrieverService.getPagedSpontaneousForms(organizationId, code, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void whenGetSpontaneousFormDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    Long spontaneousFormId = 2L;

    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);
    expectedResult.setSpontaneousFormId(spontaneousFormId);
    expectedResult.setOrganizationId(organizationId);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
          .thenReturn(expectedResult);

      SpontaneousForm result = spontaneousFormRetrieverService.getSpontaneousFormDetail(organizationId, spontaneousFormId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenSpontaneousFormMismatchOrganizationIdWhenGetSpontaneousFormDetailThenConflictException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    Long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);
    spontaneousForm.setOrganizationId(organizationId+1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
          .thenReturn(spontaneousForm);

      Assertions.assertThrows(ConflictException.class,()-> spontaneousFormRetrieverService.getSpontaneousFormDetail(organizationId, spontaneousFormId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNoSpontaneousFormWhenGetSpontaneousFormDetailThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    Long spontaneousFormId = 2L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
          .thenReturn(null);

      Assertions.assertThrows(ResourceNotFoundException.class,()-> spontaneousFormRetrieverService.getSpontaneousFormDetail(organizationId, spontaneousFormId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void whenCreateSpontaneousFormThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    PagedModelSpontaneousForm pagedModelSpontaneousForm = podamFactory.manufacturePojo(PagedModelSpontaneousForm.class);
    pagedModelSpontaneousForm.getPage().setTotalElements(0L);
    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);
    expectedResult.setSpontaneousFormId(null);
    expectedResult.setOrganizationId(organizationId);

    Mockito.doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    Mockito.when(spontaneousFormServiceMock.findAllByOrganizationIdAndCode(expectedResult.getOrganizationId(),expectedResult.getCode(),PageRequest.ofSize(1),accessToken))
        .thenReturn(pagedModelSpontaneousForm);
    Mockito.when(spontaneousFormServiceMock.createSpontaneousForm(expectedResult, accessToken))
        .thenReturn(expectedResult);

    SpontaneousForm result = spontaneousFormRetrieverService.createSpontaneousForm(organizationId, expectedResult, loggedUser, accessToken);

    assertNotNull(result);
    assertSame(expectedResult, result);
  }

  @Test
  void givenExistingSpontaneousFormWhenCreateSpontaneousFormThenConflictException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    PagedModelSpontaneousForm pagedModelSpontaneousForm = podamFactory.manufacturePojo(PagedModelSpontaneousForm.class);
    pagedModelSpontaneousForm.getPage().setTotalElements(1L);
    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);
    expectedResult.setSpontaneousFormId(null);
    expectedResult.setOrganizationId(organizationId);

    Mockito.doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);
    Mockito.when(spontaneousFormServiceMock.findAllByOrganizationIdAndCode(expectedResult.getOrganizationId(),expectedResult.getCode(),PageRequest.ofSize(1),accessToken))
        .thenReturn(pagedModelSpontaneousForm);

    assertThrows(ConflictException.class, ()-> spontaneousFormRetrieverService.createSpontaneousForm(organizationId, expectedResult, loggedUser, accessToken));
  }

  @Test
  void givenWrongOrganizationIdWhenCreateSpontaneousFormThenValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);
    expectedResult.setSpontaneousFormId(null);
    expectedResult.setOrganizationId(organizationId+1);

    Mockito.doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);

    assertThrows(ValidationException.class, ()-> spontaneousFormRetrieverService.createSpontaneousForm(organizationId, expectedResult, loggedUser, accessToken));
  }

  @Test
  void givenPopulatedSpontaneousFormIdWhenCreateSpontaneousFormThenValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);

    Mockito.doNothing().when(authorizationServiceMock).validateOrganizationOrBrokerAdmin(organizationId,loggedUser,accessToken);

    assertThrows(ValidationException.class, ()-> spontaneousFormRetrieverService.createSpontaneousForm(organizationId, expectedResult, loggedUser, accessToken));
  }
}
