package it.gov.pagopa.pu.bff.service.assessments;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsDetailService;
import it.gov.pagopa.pu.bff.connector.classification.AssessmentsService;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailRetrieverServiceImplTest {

  @Mock
  private AssessmentsService assessmentsServiceMock;
  @Mock
  private DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverServiceMock;
  @Mock
  private AssessmentsDetailService assessmentsDetailServiceMock;
  private AssessmentsDetailRetrieverService assessmentsRetrieverService;
  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    assessmentsRetrieverService = new AssessmentsDetailRetrieverServiceImpl(assessmentsServiceMock, debtPositionTypeOrgRetrieverServiceMock, assessmentsDetailServiceMock);
  }

  @Test
  void whenCreateAssessmentsDetailThenOk() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    List<AssessmentsDetail> expectedResult = podamFactory.manufacturePojo(List.class, AssessmentsDetail.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentsId, accessToken)).thenReturn(assessments);
      doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId, assessments.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
      doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateIuds(organizationId, assessments.getDebtPositionTypeOrgCode(), createAssessmentsDetail.getIuds(), accessToken);
      when(assessmentsDetailServiceMock.createAssessmentsDetail(organizationId, assessmentsId, createAssessmentsDetail, accessToken))
        .thenReturn(expectedResult);

      List<AssessmentsDetail> result = assessmentsRetrieverService.createAssessmentsDetail(
        organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken
      );

      Assertions.assertNotNull(result);
      Assertions.assertEquals(expectedResult, result);

      authorizationServiceMockedStatic.verify(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      );
    }
  }

  @Test
  void givenWrongAssessmentsOrganizationIdWhenCreateAssessmentsDetailThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId + 1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentsId, accessToken)).thenReturn(assessments);

      assertThrows(ResourceNotFoundException.class, () -> assessmentsRetrieverService.createAssessmentsDetail(
        organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken));

      verifyNoInteractions(assessmentsDetailServiceMock, debtPositionTypeOrgRetrieverServiceMock);
      authorizationServiceMockedStatic.verify(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      );
    }
  }

  @Test
  void givenNoAssessmentsWhenCreateAssessmentsDetailThenResourceNotFoundException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentsId, accessToken)).thenReturn(null);

      assertThrows(ResourceNotFoundException.class, () -> assessmentsRetrieverService.createAssessmentsDetail(
        organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken));

      verifyNoInteractions(assessmentsDetailServiceMock, debtPositionTypeOrgRetrieverServiceMock);
      authorizationServiceMockedStatic.verify(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      );
    }
  }


  @Test
  void givenEmptyIudsWhenCreateAssessmentsDetailThenIllegalArgumentException() {
    UserInfo loggedUser = new UserInfo();
    loggedUser.setUserId("user-123");
    loggedUser.setMappedExternalUserId("mappedExternalUserId");
    String accessToken = "accessToken";
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    CreateAssessmentsDetail createAssessmentsDetail = podamFactory.manufacturePojo(CreateAssessmentsDetail.class);
    createAssessmentsDetail.setIuds(Collections.emptySet());

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);


      assertThrows(IllegalArgumentException.class, () -> assessmentsRetrieverService.createAssessmentsDetail(
        organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken));

      verifyNoInteractions(assessmentsDetailServiceMock, debtPositionTypeOrgRetrieverServiceMock, assessmentsServiceMock);
      authorizationServiceMockedStatic.verify(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      );
    }
  }

  @Test
  void givenValidIdsWhenDeleteAssessmentsDetailsThenOk() {
    Long organizationId = 1L;
    String accessToken = "accessToken";
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    List<Long> ids = List.of(10L, 20L);

    Map<Long, AssessmentsDetail> detailsMap = new HashMap<>();
    for (Long id : ids) {
      AssessmentsDetail detail = podamFactory.manufacturePojo(AssessmentsDetail.class);
      detail.setOrganizationId(organizationId);
      detail.setAssessmentDetailId(id);
      detailsMap.put(id, detail);
    }

    try (MockedStatic<AuthorizationService> authMock = Mockito.mockStatic(AuthorizationService.class)) {
      authMock.when(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      for (Long id : ids) {
        AssessmentsDetail detail = detailsMap.get(id);
        when(assessmentsServiceMock.findAssessmentsDetail(id, accessToken))
          .thenReturn(detail);

        doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(
          organizationId, detail.getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);

        doNothing().when(assessmentsDetailServiceMock).deleteAssessmentsDetails(id, accessToken);
      }

      assessmentsRetrieverService.deleteAssessmentsDetails(organizationId, ids, loggedUser, accessToken);

      authMock.verify(() -> AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));

      for (Long id : ids) {
        verify(assessmentsServiceMock).findAssessmentsDetail(id, accessToken);
        verify(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId, detailsMap.get(id).getDebtPositionTypeOrgCode(), loggedUser.getMappedExternalUserId(), accessToken);
        verify(assessmentsDetailServiceMock).deleteAssessmentsDetails(id, accessToken);
      }
    }
  }

  @Test
  void givenInvalidIdWhenDeleteAssessmentsDetailsThenThrowResourceNotFoundException() {
    Long organizationId = 1L;
    String accessToken = "accessToken";
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    List<Long> ids = List.of(10L, 20L);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
          AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      when(assessmentsServiceMock.findAssessmentsDetail(10L, accessToken)).thenReturn(null);

      assertThrows(ResourceNotFoundException.class, () ->
        assessmentsRetrieverService.deleteAssessmentsDetails(organizationId, ids, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }

  @Test
  void givenIdBelongingToDifferentOrganizationWhenDeleteAssessmentsDetailsThenThrowResourceNotFoundException() {
    Long organizationId = 1L;
    String accessToken = "accessToken";
    UserInfo loggedUser = podamFactory.manufacturePojo(UserInfo.class);
    List<Long> ids = List.of(10L);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
          AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser))
        .thenAnswer(a -> null);

      AssessmentsDetail detail = podamFactory.manufacturePojo(AssessmentsDetail.class);
      detail.setOrganizationId(organizationId + 1);
      when(assessmentsServiceMock.findAssessmentsDetail(10L, accessToken)).thenReturn(detail);

      assertThrows(ResourceNotFoundException.class, () ->
        assessmentsRetrieverService.deleteAssessmentsDetails(organizationId, ids, loggedUser, accessToken));

      authorizationServiceMockedStatic.verify(() ->
        AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser));
    }
  }
}
