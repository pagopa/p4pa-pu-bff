package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsClient;
import it.gov.pagopa.pu.bff.connector.classification.client.AssessmentsEntityExtendedClient;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentsServiceImplTest {

  @Mock
  private AssessmentsClient assessmentsClientMock;
  @Mock
  private AssessmentsEntityExtendedClient assessmentsEntityExtendedClientMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private AssessmentsService assessmentsService;

  @BeforeEach
  void setUp() {
    assessmentsService = new AssessmentsServiceImpl(assessmentsClientMock,assessmentsEntityExtendedClientMock);
  }

  @AfterEach
  void afterEach(){
    Mockito.verifyNoMoreInteractions(assessmentsClientMock,assessmentsEntityExtendedClientMock);
  }

  @Test
  void givenParametersWhenFindPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    //given
    String accessToken = "accessToken";
    AssessmentsFiltersDTO assessmentsFiltersDTO = podamFactory.manufacturePojo(AssessmentsFiltersDTO.class);
    PagedAssessmentsView pagedAssessmentsView = podamFactory.manufacturePojo(PagedAssessmentsView.class);
    Mockito.when(assessmentsClientMock.findPagedAssessmentsView(assessmentsFiltersDTO, Pageable.ofSize(1), accessToken)).thenReturn(pagedAssessmentsView);

    //when
    PagedAssessmentsView result = assessmentsService.findPagedAssessmentsView(assessmentsFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedAssessmentsView, result);
  }

  @Test
  void givenParametersWhenFindPagedModelAssessmentsDetailThenReturnPagedModelAssessmentsDetail() {
    //given
    String accessToken = "accessToken";
    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(PagedModelAssessmentsDetail.class);

    Mockito.when(assessmentsClientMock.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), accessToken)).thenReturn(pagedModelAssessmentsDetail);
    //when
    PagedModelAssessmentsDetail result = assessmentsService.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedModelAssessmentsDetail, result);
  }

  @Test
  void givenIdWhenFindAssessmentsDetailThenReturnAssessmentDetail() {
    //given
    String accessToken = "accessToken";
    Long assessmentDetailId = 1L;
    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);
    Mockito.when(assessmentsClientMock.findAssessmentsDetail(assessmentDetailId,accessToken)).thenReturn(assessmentsDetail);
    //when
    AssessmentsDetail result = assessmentsService.findAssessmentsDetail(assessmentDetailId, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(assessmentsDetail, result);
  }

  @Test
  void givenOrganizationIdAssessmentNameAndDebtPositionTypeOrgCodeWhenCreateAssessmentThenReturnAssessment() {
    //given
    String accessToken = "accessToken";
    Long organizationId = 1L;
    String assessmentName = "testName";
    String debtPositionTypeOrgCode = "Code";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    Mockito.when(assessmentsClientMock.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, accessToken)).thenReturn(assessments);
    //when
    Assessments result = assessmentsService.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(assessments, result);
  }

  @Test
  void whenGetAssessmentsByIdThenInvokeClient() {
    long assessmentId = 1L;
    String accessToken = "ACCESSTOKEN";
    Assessments expectedResult = new Assessments();

    when(assessmentsClientMock.getAssessmentsById(assessmentId, accessToken))
            .thenReturn(expectedResult);

    Assessments result = assessmentsService.getAssessmentsById(assessmentId, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenUpdateStatusThenInvokeClient() {
    Long organizationId = 1L;
    Long assessmentId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    String accessToken = "ACCESSTOKEN";

    doNothing().when(assessmentsEntityExtendedClientMock).updateStatus(organizationId,assessmentId,status,accessToken);

    assessmentsService.updateStatus(organizationId,assessmentId,status,accessToken);

    verifyNoMoreInteractions(assessmentsEntityExtendedClientMock);
  }
}
