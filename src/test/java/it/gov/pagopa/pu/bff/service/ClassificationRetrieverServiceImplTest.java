package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.connector.classification.ClassificationService;
import it.gov.pagopa.pu.bff.dto.*;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.ClassificationDetailDTOMapper;
import it.gov.pagopa.pu.bff.service.classification.ClassificationRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationRetrieverServiceImplTest {

  @Mock
  private ClassificationService classificationServiceMock;
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;
  @Mock
  private ClassificationDetailDTOMapper classificationDetailDTOMapperMock;
  @Mock
  private AssessmentsService assessmentsServiceMock;

  private ClassificationRetrieverServiceImpl classificationRetrieverService;

  private final String accessToken = "TOKEN";
  public static final int PAGE_MAX_SIZE = 10;

  @BeforeEach
  void setUp() {
    classificationRetrieverService = new ClassificationRetrieverServiceImpl(
      classificationServiceMock, debtPositionTypeOrgRetrieverServiceMock,classificationDetailDTOMapperMock, assessmentsServiceMock, PAGE_MAX_SIZE);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
            classificationServiceMock,
            classificationDetailDTOMapperMock,
            debtPositionTypeOrgRetrieverServiceMock,
            assessmentsServiceMock
    );
  }

  @Test
  void givenPopulatedDebtPositionTypeOrgCodeFilterWhenGetTreasuredClassificationThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    treasuredClassificationFiltersDTO.setIuv("IUV123");
    treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification expectedResult = new PagedTreasuredClassification();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId,debtPositionTypeOrgCode,loggedUser.getMappedExternalUserId(),accessToken);
      when(classificationServiceMock.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken))
        .thenReturn(expectedResult);

      PagedTreasuredClassification result = classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, debtPositionTypeOrgCode, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNoDebtPositionTypeOrgCodeFilterWhenGetTreasuredClassificationThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    treasuredClassificationFiltersDTO.setIuv("IUV123");
    treasuredClassificationFiltersDTO.setDebtPositionTypeOrgCodes(Collections.singleton(debtPositionTypeOrgCode));
    PageRequest pageable = PageRequest.of(0, 10);
    PagedTreasuredClassification expectedResult = new PagedTreasuredClassification();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgCodes(organizationId,loggedUser.getMappedExternalUserId(),accessToken))
              .thenReturn(treasuredClassificationFiltersDTO.getDebtPositionTypeOrgCodes());
      when(classificationServiceMock.getTreasuredClassifications(organizationId, treasuredClassificationFiltersDTO, pageable, accessToken))
              .thenReturn(expectedResult);

      PagedTreasuredClassification result = classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, null, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNoDebtPositionTypeOrgCodesWhenGetTreasuredClassificationThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    TreasuredClassificationFiltersDTO treasuredClassificationFiltersDTO = new TreasuredClassificationFiltersDTO();
    treasuredClassificationFiltersDTO.setIuv("IUV123");
    PageRequest pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgCodes(organizationId,loggedUser.getMappedExternalUserId(),accessToken))
              .thenReturn(null);

      assertThrows(ResourceNotFoundException.class,()->classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, null, pageable, loggedUser, accessToken));

      verifyNoInteractions(classificationServiceMock);
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
        classificationRetrieverService.getTreasuredClassification(organizationId, filtersDTO, null,pageable, loggedUser, accessToken));

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
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    Pageable pageable = PageRequest.of(0, 10);
    long organizationId = 1L;
    filtersDTO.setDebtPositionTypeOrgCodes(Collections.singleton("debtPositionTypeOrgCode"));

    PagedTreasuredClassification expectedResult = new PagedTreasuredClassification();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);
      when(debtPositionTypeOrgRetrieverServiceMock.getDebtPositionTypeOrgCodes(organizationId,loggedUser.getMappedExternalUserId(),accessToken))
              .thenReturn(filtersDTO.getDebtPositionTypeOrgCodes());
      Mockito.when(classificationServiceMock.getTreasuredClassifications(organizationId, filtersDTO, pageable, accessToken))
        .thenReturn(expectedResult);

      PagedTreasuredClassification result = classificationRetrieverService.getTreasuredClassification(organizationId, filtersDTO, null, pageable, loggedUser, accessToken);

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
        classificationRetrieverService.getTreasuredClassification(organizationId, treasuredClassificationFiltersDTO, null, pageable, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
    verifyNoInteractions(classificationServiceMock);
  }

  @Test
  void givenValidUserWhenGetClassificationDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long classificationId = 123L;
    ClassificationDetailViewDTO classificationDetailViewDTO = new ClassificationDetailViewDTO();
    classificationDetailViewDTO.setDebtPositionTypeOrgCode("debtPositionTypeOrgCode");
    ClassificationDetailDTO expectedResult = new ClassificationDetailDTO();

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(classificationServiceMock.getClassificationDetail(organizationId, classificationId, accessToken))
        .thenReturn(classificationDetailViewDTO);
      doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId,classificationDetailViewDTO.getDebtPositionTypeOrgCode(),loggedUser.getMappedExternalUserId(),accessToken);
      when(classificationDetailDTOMapperMock.map(classificationDetailViewDTO))
        .thenReturn(expectedResult);

      ClassificationDetailViewDTO result = classificationRetrieverService.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenNoDebtPositionTypeOrgCodeWhenGetClassificationDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

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
      verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock);
    }
  }

  @Test
  void givenNoClassificationWhenGetClassificationDetailThenNull() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");

    long organizationId = 1L;
    long classificationId = 123L;

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)).thenAnswer(a -> null);

      when(classificationServiceMock.getClassificationDetail(organizationId, classificationId, accessToken))
        .thenReturn(null);
      when(classificationDetailDTOMapperMock.map(null))
        .thenReturn(null);

      ClassificationDetailViewDTO result = classificationRetrieverService.getClassificationDetail(organizationId, classificationId, loggedUser, accessToken);

      assertNull(result);

      authorizationServiceMockedStatic.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
      verifyNoInteractions(debtPositionTypeOrgRetrieverServiceMock);
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

  @Test
  void givenValidFiltersWhenGetPaidInstallmentsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long assessmentId = 42L;

    ClassificationPaidInstallmentsFiltersDTO filters = ClassificationPaidInstallmentsFiltersDTO.builder()
      .iuv("IUV123")
      .build();

    Pageable pageable = PageRequest.of(0, 10);
    PagedClassificationPaidInstallmentsView expectedResult = new PagedClassificationPaidInstallmentsView();

    PagedModelAssessmentsDetail assessmentsDetailPage = new PagedModelAssessmentsDetail();
    PagedModelAssessmentsDetailEmbedded embedded = new PagedModelAssessmentsDetailEmbedded();
    AssessmentsDetail detail = new AssessmentsDetail();
    detail.setIud("IUD123");
    embedded.setAssessmentsDetails(List.of(detail));
    assessmentsDetailPage.setEmbedded(embedded);

    Assessments assessment = new Assessments();
    assessment.setAssessmentId(assessmentId);
    assessment.setOrganizationId(organizationId);

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken))
        .thenReturn(assessment);

      when(assessmentsServiceMock.findPagedModelAssessmentsDetail(argThat(f->f.getAssessmentId().equals(assessmentId)),
              argThat(p->p.getPageNumber()==0 && p.getPageSize() == PAGE_MAX_SIZE), eq(accessToken)))
        .thenReturn(assessmentsDetailPage);

      when(classificationServiceMock.getPaidInstallments(eq(organizationId), argThat(f->f.getIuv().equals(filters.getIuv()) && f.getIuds().equals(Collections.singleton(detail.getIud()))), eq(pageable), eq(accessToken)))
        .thenReturn(expectedResult);

      PagedClassificationPaidInstallmentsView result =
        classificationRetrieverService.getPaidInstallments(organizationId, assessmentId, filters, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
    }
  }

  @Test
  void givenNoAssessmentIdWhenGetPaidInstallmentsThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;

    ClassificationPaidInstallmentsFiltersDTO filters = ClassificationPaidInstallmentsFiltersDTO.builder()
      .iuv("IUV123")
      .build();

    Pageable pageable = PageRequest.of(0, 10);
    PagedClassificationPaidInstallmentsView expectedResult = new PagedClassificationPaidInstallmentsView();

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(classificationServiceMock.getPaidInstallments(organizationId, filters, pageable, accessToken))
        .thenReturn(expectedResult);

      PagedClassificationPaidInstallmentsView result =
        classificationRetrieverService.getPaidInstallments(organizationId, null, filters, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);

      verifyNoInteractions(assessmentsServiceMock);
    }
  }

  @Test
  void givenInvalidFiltersWhenGetPaidInstallmentsThenThrowsIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long assessmentId = 42L;

    ClassificationPaidInstallmentsFiltersDTO filters = ClassificationPaidInstallmentsFiltersDTO.builder()
      .build();

    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
        classificationRetrieverService.getPaidInstallments(organizationId, assessmentId, filters, pageable, loggedUser, accessToken)
      );

      assertEquals("At least one filter must be provided, and all date intervals must have both 'from' and 'to' set or be null", ex.getMessage());
      verifyNoInteractions(assessmentsServiceMock, classificationServiceMock);
    }
  }

  @Test
  void givenNoAssessmentsDetailsWhenGetPaidInstallmentsThenOkWithEmptyIuds() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long assessmentId = 42L;

    ClassificationPaidInstallmentsFiltersDTO filters = ClassificationPaidInstallmentsFiltersDTO.builder()
      .iuv("IUV123")
      .build();

    Pageable pageable = PageRequest.of(0, 10);
    PagedClassificationPaidInstallmentsView expectedResult = new PagedClassificationPaidInstallmentsView();

    PagedModelAssessmentsDetail emptyPage = new PagedModelAssessmentsDetail();
    emptyPage.setEmbedded(new PagedModelAssessmentsDetailEmbedded());

    Assessments assessment = new Assessments();
    assessment.setAssessmentId(assessmentId);
    assessment.setOrganizationId(organizationId);

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken))
        .thenReturn(assessment);

      when(assessmentsServiceMock.findPagedModelAssessmentsDetail(argThat(f->f.getAssessmentId().equals(assessmentId)),
              argThat(p->p.getPageNumber()==0 && p.getPageSize() == PAGE_MAX_SIZE), eq(accessToken)))
        .thenReturn(emptyPage);

      when(classificationServiceMock.getPaidInstallments(organizationId, filters, pageable, accessToken))
        .thenReturn(expectedResult);

      PagedClassificationPaidInstallmentsView result =
        classificationRetrieverService.getPaidInstallments(organizationId, assessmentId, filters, pageable, loggedUser, accessToken);

      assertNotNull(result);
      assertSame(expectedResult, result);
      assertTrue(filters.getIuds().isEmpty());
    }
  }

  @Test
  void givenAssessmentNotFoundWhenGetPaidInstallmentsThenThrowsResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long assessmentId = 42L;

    ClassificationPaidInstallmentsFiltersDTO filters = ClassificationPaidInstallmentsFiltersDTO.builder()
      .iuv("IUV123")
      .build();

    Pageable pageable = PageRequest.of(0, 10);

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken))
        .thenReturn(null);

      ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
        classificationRetrieverService.getPaidInstallments(organizationId, assessmentId, filters, pageable, loggedUser, accessToken)
      );

      assertEquals("Assessment with id " + assessmentId + " not found", ex.getMessage());
      verifyNoInteractions(classificationServiceMock);
    }
  }

  @Test
  void givenAssessmentWithDifferentOrganizationWhenGetPaidInstallmentsThenThrowsResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");

    long organizationId = 1L;
    long assessmentId = 42L;

    ClassificationPaidInstallmentsFiltersDTO filters = ClassificationPaidInstallmentsFiltersDTO.builder()
      .iuv("IUV123")
      .build();

    Pageable pageable = PageRequest.of(0, 10);

    Assessments assessment = new Assessments();
    assessment.setAssessmentId(assessmentId);
    assessment.setOrganizationId(999L);

    try (MockedStatic<AuthorizationService> authorizationMock = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentId, accessToken))
        .thenReturn(assessment);

      ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () ->
        classificationRetrieverService.getPaidInstallments(organizationId, assessmentId, filters, pageable, loggedUser, accessToken)
      );

      assertEquals("Assessment with id " + assessmentId + " not found", ex.getMessage());
      verifyNoInteractions(classificationServiceMock);
    }
  }
}

