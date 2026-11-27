package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.bff.connector.debt_position.SpontaneousFormService;
import it.gov.pagopa.pu.bff.dto.generated.PagedSpontaneousForm;
import it.gov.pagopa.pu.bff.dto.generated.SpontaneousFormDetailDTO;
import it.gov.pagopa.pu.bff.exception.common.ConflictException;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.mapper.PagedSpontaneousFormMapper;
import it.gov.pagopa.pu.bff.mapper.SpontaneousFormDetailDTOMapper;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverService;
import it.gov.pagopa.pu.bff.service.spontaneous_form.SpontaneousFormRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import jakarta.validation.ValidationException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpontaneousFormRetrieverServiceImplTest {

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  @Mock
  private SpontaneousFormService spontaneousFormServiceMock;
  @Mock
  private PagedSpontaneousFormMapper pagedSpontaneousFormMapperMock;
  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private SpontaneousFormDetailDTOMapper spontaneousFormDetailDTOMapperMock;

  private SpontaneousFormRetrieverService spontaneousFormRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    spontaneousFormRetrieverService = new SpontaneousFormRetrieverServiceImpl(spontaneousFormServiceMock, pagedSpontaneousFormMapperMock, authorizationServiceMock, debtPositionTypeOrgServiceMock, spontaneousFormDetailDTOMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(spontaneousFormServiceMock, pagedSpontaneousFormMapperMock, authorizationServiceMock, debtPositionTypeOrgServiceMock, spontaneousFormDetailDTOMapperMock);
  }

  @Test
  void givenValidUserWhenGetSpontaneousFormsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;

    List<SpontaneousForm> expectedResult = podamFactory.manufacturePojo(List.class,SpontaneousForm.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(spontaneousFormServiceMock.findAllByOrganizationId(organizationId,accessToken))
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

    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
      .thenReturn(spontaneousForm);

    SpontaneousForm result = spontaneousFormRetrieverService
      .getSpontaneousFormAndValidate(spontaneousFormId, debtPositionTypeOrg, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertSame(spontaneousForm, result);

    verify(spontaneousFormServiceMock).getSpontaneousForm(spontaneousFormId, accessToken);
  }

  @Test
  void givenSpontaneousFormNotFoundWhenGetSpontaneousFormAndValidateThenThrowsException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long debtPositionTypeId = 10L;
    long spontaneousFormId = 999L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);
    debtPositionTypeOrg.setSpontaneousFormId(spontaneousFormId);

    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId,accessToken))
      .thenReturn(null);

    NotFoundException ex = Assertions.assertThrows(
      NotFoundException.class,
      () -> spontaneousFormRetrieverService.getSpontaneousFormAndValidate(
        spontaneousFormId, debtPositionTypeOrg, accessToken
      )
    );

    Assertions.assertEquals(
      "SpontaneousForm with id 999 not found",
      ex.getMessage()
    );
  }

  @Test
  void givenNoSpontaneousFormIdWhenGetSpontaneousFormAndValidateThenThrowsValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long debtPositionTypeId = 10L;

    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setDebtPositionTypeId(debtPositionTypeId);

    ValidationException ex = Assertions.assertThrows(
      ValidationException.class,
      () -> spontaneousFormRetrieverService.getSpontaneousFormAndValidate(
        null, debtPositionTypeOrg, accessToken
      )
    );

    Assertions.assertEquals(
      "SpontaneousFormId must not be null",
      ex.getMessage()
    );
    Mockito.verifyNoInteractions(spontaneousFormServiceMock);
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

    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId,accessToken))
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

      when(spontaneousFormServiceMock.findAllByOrganizationIdAndCode(organizationId, code, pageable, accessToken))
          .thenReturn(pagedModelSpontaneousForm);
      when(pagedSpontaneousFormMapperMock.map(pagedModelSpontaneousForm))
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

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    SpontaneousFormDetailDTO expectedResult = podamFactory.manufacturePojo(SpontaneousFormDetailDTO.class);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);
    spontaneousForm.setOrganizationId(organizationId);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
          .thenReturn(spontaneousForm);
      when(spontaneousFormDetailDTOMapperMock.map(spontaneousForm)).thenReturn(expectedResult);

      SpontaneousFormDetailDTO result = spontaneousFormRetrieverService.getSpontaneousFormDetail(organizationId, spontaneousFormId, loggedUser, accessToken);

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

      when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
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

      when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
          .thenReturn(null);

      Assertions.assertThrows(NotFoundException.class,()-> spontaneousFormRetrieverService.getSpontaneousFormDetail(organizationId, spontaneousFormId, loggedUser, accessToken));

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

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.findAllByOrganizationIdAndCode(expectedResult.getOrganizationId(),expectedResult.getCode(),PageRequest.ofSize(1),accessToken))
        .thenReturn(pagedModelSpontaneousForm);
    when(spontaneousFormServiceMock.createSpontaneousForm(expectedResult, accessToken))
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

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.findAllByOrganizationIdAndCode(expectedResult.getOrganizationId(),expectedResult.getCode(),PageRequest.ofSize(1),accessToken))
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

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);

    assertThrows(ValidationException.class, ()-> spontaneousFormRetrieverService.createSpontaneousForm(organizationId, expectedResult, loggedUser, accessToken));
  }

  @Test
  void givenPopulatedSpontaneousFormIdWhenCreateSpontaneousFormThenValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    SpontaneousForm expectedResult = podamFactory.manufacturePojo(SpontaneousForm.class);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);

    assertThrows(ValidationException.class, ()-> spontaneousFormRetrieverService.createSpontaneousForm(organizationId, expectedResult, loggedUser, accessToken));
  }

  @Test
  void whenDeleteSpontaneousFormThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = new SpontaneousForm();
    spontaneousForm.setOrganizationId(organizationId);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
        .thenReturn(spontaneousForm);
    when(debtPositionTypeOrgServiceMock.isSpontaneousFormReferencedByDpto(spontaneousFormId, accessToken))
        .thenReturn(false);
    Mockito.doNothing().when(spontaneousFormServiceMock).deleteSpontaneousForm(spontaneousFormId,accessToken);

    assertDoesNotThrow(()->spontaneousFormRetrieverService.deleteSpontaneousForm(organizationId, spontaneousFormId, loggedUser, accessToken));
  }

  @Test
  void givenReferencedSpontaneousFormWhenDeleteSpontaneousFormThenConflictException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = new SpontaneousForm();
    spontaneousForm.setOrganizationId(organizationId);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
        .thenReturn(spontaneousForm);
    when(debtPositionTypeOrgServiceMock.isSpontaneousFormReferencedByDpto(spontaneousFormId, accessToken))
        .thenReturn(true);

    assertThrows(ConflictException.class,()->spontaneousFormRetrieverService.deleteSpontaneousForm(organizationId, spontaneousFormId, loggedUser, accessToken));
  }

  @Test
  void givenWrongOrganizationIdWhenDeleteSpontaneousFormThenConflictException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = new SpontaneousForm();
    spontaneousForm.setOrganizationId(organizationId+1);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
        .thenReturn(spontaneousForm);

    assertThrows(ConflictException.class,()->spontaneousFormRetrieverService.deleteSpontaneousForm(organizationId, spontaneousFormId, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
  }

  @Test
  void givenNoSpontaneousFormWhenDeleteSpontaneousFormThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
        .thenReturn(null);

    assertThrows(NotFoundException.class,()->spontaneousFormRetrieverService.deleteSpontaneousForm(organizationId, spontaneousFormId, loggedUser, accessToken));

    Mockito.verifyNoInteractions(debtPositionTypeOrgServiceMock);
  }

  @Test
  void whenUpdateSpontaneousFormThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);
    spontaneousForm.setOrganizationId(organizationId);
    SpontaneousForm existingSpontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    existingSpontaneousForm.setSpontaneousFormId(spontaneousFormId);
    existingSpontaneousForm.setOrganizationId(organizationId);
    existingSpontaneousForm.setCode(spontaneousForm.getCode());

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
      .thenReturn(existingSpontaneousForm);
    Mockito.doNothing().when(spontaneousFormServiceMock).updateSpontaneousForm(spontaneousForm,accessToken);

    assertDoesNotThrow(()->spontaneousFormRetrieverService.updateSpontaneousForm(organizationId, spontaneousForm, loggedUser, accessToken));
  }

  @Test
  void givenUpdatedReadOnlyFieldsWhenUpdateSpontaneousFormThenValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);
    spontaneousForm.setOrganizationId(organizationId);
    SpontaneousForm existingSpontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    existingSpontaneousForm.setSpontaneousFormId(spontaneousFormId);
    existingSpontaneousForm.setOrganizationId(organizationId);
    existingSpontaneousForm.setCode(spontaneousForm.getCode()+1);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
      .thenReturn(existingSpontaneousForm);

    assertThrows(ValidationException.class,()->spontaneousFormRetrieverService.updateSpontaneousForm(organizationId, spontaneousForm, loggedUser, accessToken));
  }

  @Test
  void givenMismatchingOrganizationIdWhenUpdateSpontaneousFormThenConflictException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);
    spontaneousForm.setOrganizationId(organizationId);
    SpontaneousForm existingSpontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    existingSpontaneousForm.setSpontaneousFormId(spontaneousFormId);
    existingSpontaneousForm.setOrganizationId(organizationId+1);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
      .thenReturn(existingSpontaneousForm);

    assertThrows(ConflictException.class,()->spontaneousFormRetrieverService.updateSpontaneousForm(organizationId, spontaneousForm, loggedUser, accessToken));
  }

  @Test
  void givenNoExistingSpontaneousFormWhenUpdateSpontaneousFormThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;
    long spontaneousFormId = 2L;

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    spontaneousForm.setSpontaneousFormId(spontaneousFormId);
    spontaneousForm.setOrganizationId(organizationId);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);
    when(spontaneousFormServiceMock.getSpontaneousForm(spontaneousFormId, accessToken))
      .thenReturn(null);

    assertThrows(NotFoundException.class,()->spontaneousFormRetrieverService.updateSpontaneousForm(organizationId, spontaneousForm, loggedUser, accessToken));
  }

  @Test
  void givenNoSpontaneousFormIdWhenUpdateSpontaneousFormThenValidationException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    long organizationId = 1L;

    SpontaneousForm spontaneousForm = podamFactory.manufacturePojo(SpontaneousForm.class);
    spontaneousForm.setSpontaneousFormId(null);
    spontaneousForm.setOrganizationId(organizationId);

    Mockito.doNothing().when(authorizationServiceMock).validateAdminRole(organizationId,loggedUser);

    assertThrows(ValidationException.class,()->spontaneousFormRetrieverService.updateSpontaneousForm(organizationId, spontaneousForm, loggedUser, accessToken));

    Mockito.verifyNoInteractions(spontaneousFormServiceMock);
  }
}
