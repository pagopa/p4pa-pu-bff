package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.TreasuredClassificationFiltersDTO;
import it.gov.pagopa.pu.bff.mapper.ClassificationDetailDTOMapper;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverServiceImpl;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
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
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationRetrieverServiceImplTest {

  @Mock
  private ClassificationService classificationServiceMock;
  @Mock
  private ClassificationDetailDTOMapper classificationDetailDTOMapperMock;

  private ClassificationRetrieverServiceImpl classificationRetrieverService;

  private final String accessToken = "TOKEN";

  @BeforeEach
  void setUp() {
    classificationRetrieverService = new ClassificationRetrieverServiceImpl(classificationServiceMock,classificationDetailDTOMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
            classificationServiceMock,
            classificationDetailDTOMapperMock
    );
  }

  @Test
  void givenValidUserWhenGetTreasuredClassificationThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    treasuredClassificationFiltersDTO.setIuv("IUV123");
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
    }
  }

  @Test
  void givenLabelOnlyWhenGetTreasuredClassificationThenOk() {
    testSingleFilterSuccess(TreasuredClassificationFiltersDTO.builder().label(ClassificationsEnum.DOPPI).build());
  }

  @Test
  void givenIudOnlyWhenGetTreasuredClassificationThenOk() {
    testSingleFilterSuccess(TreasuredClassificationFiltersDTO.builder().iud("IUD123").build());
  }

  @Test
  void givenIuvOnlyWhenGetTreasuredClassificationThenOk() {
    testSingleFilterSuccess(TreasuredClassificationFiltersDTO.builder().iuv("IUV123").build());
  }

  @Test
  void givenIurOnlyWhenGetTreasuredClassificationThenOk() {
    testSingleFilterSuccess(TreasuredClassificationFiltersDTO.builder().iur("IUR123").build());
  }

  @Test
  void givenValidLastClassificationDateWhenGetTreasuredClassificationThenOk() {
    LocalDateIntervalFilter lastClassificationDate = new LocalDateIntervalFilter(LocalDate.now().minusDays(2), LocalDate.now());
    testSingleFilterSuccess(TreasuredClassificationFiltersDTO.builder().lastClassificationDate(lastClassificationDate).build());
  }

  @Test
  void givenAllDateRangesEmptyWhenGetTreasuredClassificationThenThrowIllegalArgumentException() {
    TreasuredClassificationFiltersDTO filtersDTO = new TreasuredClassificationFiltersDTO(
      null, null, null, null,
      new LocalDateIntervalFilter(null, null),
      new LocalDateIntervalFilter(null, null),
      new OffsetDateTimeIntervalFilter(null, null),
      new LocalDateIntervalFilter(null, null),
      new LocalDateIntervalFilter(null, null),
      new LocalDateIntervalFilter(null, null),
      null, null, null, null, null,
      null, null, null, null, null,
      null, null, null, null, null
    );
    assertThrowsIllegalArgument(filtersDTO);
  }

  private void assertThrowsIllegalArgument(TreasuredClassificationFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);
    long organizationId = 1L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
        classificationRetrieverService.getTreasuredClassification(organizationId, filtersDTO, pageable, loggedUser, accessToken));

      assertEquals("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null", exception.getMessage());
    }

    Mockito.verifyNoInteractions(classificationServiceMock);
  }

  @Test
  void givenOnlyIuvWhenGetTreasuredClassificationThenOk() {
    TreasuredClassificationFiltersDTO filtersDTO = new TreasuredClassificationFiltersDTO(
      null, null, "IUV123", null,
      null, null, null, null, null, null,
      null, null, null, null, null,
      null, null, null, null, null,
      null, null, null, null, null
    );
    testSingleFilterSuccess(filtersDTO);
  }

  private void testSingleFilterSuccess(TreasuredClassificationFiltersDTO filtersDTO) {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    Pageable pageable = PageRequest.of(0, 10);
    long organizationId = 1L;

    PagedTreasuredClassification expectedResult = new PagedTreasuredClassification();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      Mockito.when(classificationServiceMock.getTreasuredClassifications(organizationId, filtersDTO, pageable, accessToken))
        .thenReturn(expectedResult);

      PagedTreasuredClassification result = classificationRetrieverService.getTreasuredClassification(organizationId, filtersDTO, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
    }
  }

  @Test
  void givenInvalidUserWhenGetTreasuredClassificationThenAuthorizationDeniedException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    PageRequest pageable = PageRequest.of(0, 10);

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
    ClassificationDetailViewDTO classificationDetailViewDTO = new ClassificationDetailViewDTO();
    ClassificationDetailDTO expectedResult = new ClassificationDetailDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(classificationServiceMock.getClassificationDetail(organizationId, classificationId, accessToken))
        .thenReturn(classificationDetailViewDTO);
      when(classificationDetailDTOMapperMock.map(classificationDetailViewDTO))
        .thenReturn(expectedResult);

      ClassificationDetailViewDTO result = classificationRetrieverService.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
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
    verifyNoInteractions(classificationServiceMock, classificationDetailDTOMapperMock);
  }
}

