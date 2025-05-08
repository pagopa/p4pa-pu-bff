package it.gov.pagopa.pu.bff.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverServiceImpl;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;

@ExtendWith(MockitoExtension.class)
class ClassificationRetrieverServiceImplTest {

  @Mock
  private ClassificationService classificationServiceMock;

  private ClassificationRetrieverServiceImpl classificationRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    classificationRetrieverService = new ClassificationRetrieverServiceImpl(classificationServiceMock);
  }

  @Test
  void givenValidUserWhenGetTreasuredClassificationThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification expectedResult = new PagedTreasuredClassification();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(classificationServiceMock.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken))
        .thenReturn(expectedResult);

      PagedTreasuredClassification result = classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      verify(classificationServiceMock).getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken);
      verifyNoMoreInteractions(classificationServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetTreasuredClassificationThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification expectedResult = new PagedTreasuredClassification();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    verifyNoInteractions(classificationServiceMock);
  }

  @Test
  void givenValidUserWhenGetClassificationDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long classificationId = 123L;
    ClassificationDetailViewDTO expectedClassificationDetail = new ClassificationDetailViewDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(classificationServiceMock.getClassificationDetail(organizationId, classificationId, accessToken))
        .thenReturn(expectedClassificationDetail);

      ClassificationDetailViewDTO result = classificationRetrieverService.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedClassificationDetail, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      verify(classificationServiceMock).getClassificationDetail(organizationId, classificationId, accessToken);
      verifyNoMoreInteractions(classificationServiceMock);
    }
  }

  @Test
  void givenInvalidUserWhenGetClassificationDetailThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long classificationId = 123L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenThrow(new AuthorizationDeniedException("Access denied"));

      Assertions.assertThrows(AuthorizationDeniedException.class, () ->
        classificationRetrieverService.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    verifyNoInteractions(classificationServiceMock);
  }
}

