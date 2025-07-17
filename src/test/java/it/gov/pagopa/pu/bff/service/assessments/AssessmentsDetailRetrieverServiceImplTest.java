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
import java.util.List;

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

      when(assessmentsServiceMock.getAssessmentsById(assessmentsId,accessToken)).thenReturn(assessments);
      doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateOperator(organizationId,assessments.getDebtPositionTypeOrgCode(),loggedUser.getMappedExternalUserId(),accessToken);
      doNothing().when(debtPositionTypeOrgRetrieverServiceMock).validateIuds(organizationId,assessments.getDebtPositionTypeOrgCode(), createAssessmentsDetail.getIuds(),accessToken);
      when(assessmentsDetailServiceMock.createAssessmentsDetail(organizationId,assessmentsId,createAssessmentsDetail,accessToken))
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
    assessments.setOrganizationId(organizationId+1);

    try (MockedStatic<AuthorizationService> authorizationServiceMockedStatic = Mockito.mockStatic(AuthorizationService.class)) {
      authorizationServiceMockedStatic.when(() ->
              AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      ).thenAnswer(a -> null);

      when(assessmentsServiceMock.getAssessmentsById(assessmentsId,accessToken)).thenReturn(assessments);

      assertThrows(ResourceNotFoundException.class,()-> assessmentsRetrieverService.createAssessmentsDetail(
              organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken));

      verifyNoInteractions(assessmentsDetailServiceMock,debtPositionTypeOrgRetrieverServiceMock);
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

      when(assessmentsServiceMock.getAssessmentsById(assessmentsId,accessToken)).thenReturn(null);

      assertThrows(ResourceNotFoundException.class,()-> assessmentsRetrieverService.createAssessmentsDetail(
              organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken));

      verifyNoInteractions(assessmentsDetailServiceMock,debtPositionTypeOrgRetrieverServiceMock);
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


      assertThrows(IllegalArgumentException.class,()-> assessmentsRetrieverService.createAssessmentsDetail(
              organizationId, assessmentsId, createAssessmentsDetail, loggedUser, accessToken));

      verifyNoInteractions(assessmentsDetailServiceMock,debtPositionTypeOrgRetrieverServiceMock, assessmentsServiceMock);
      authorizationServiceMockedStatic.verify(() ->
              AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser)
      );
    }
  }
}
