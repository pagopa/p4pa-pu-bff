package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsControllerApi;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsDetailEntityControllerApi;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsDetailSearchControllerApi;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsEntityControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AssessmentsClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private AssessmentsControllerApi assessmentsControllerApiMock;
  @Mock
  private AssessmentsDetailSearchControllerApi assessmentsDetailSearchControllerApiMock;
  @Mock
  private AssessmentsDetailEntityControllerApi assessmentsDetailEntityControllerApiMock;
  @Mock
  private AssessmentsEntityControllerApi assessmentsEntityControllerApiMock;

  private AssessmentsClient assessmentsClient;
  private PodamFactory podamFactory;

  @BeforeEach
  void setUp() {
    assessmentsClient = new AssessmentsClient(classificationApisHolderMock);
    podamFactory = new PodamFactoryImpl();
  }

  @AfterEach
  void afterEach() {
    Mockito.verifyNoMoreInteractions(classificationApisHolderMock, assessmentsControllerApiMock, assessmentsDetailSearchControllerApiMock, assessmentsDetailEntityControllerApiMock,assessmentsEntityControllerApiMock);
  }

  @Test
  void givenFiltersWhenFindPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    //given
    String accessToken = "accessToken";
    AssessmentsFiltersDTO assessmentsFiltersDTO = podamFactory.manufacturePojo(AssessmentsFiltersDTO.class);
    PagedAssessmentsView pagedAssessmentsView = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(classificationApisHolderMock.getAssessmentsControllerApi(accessToken)).thenReturn(assessmentsControllerApiMock);
    Mockito.when(assessmentsControllerApiMock.getPagedAssessmentsList(assessmentsFiltersDTO.getAssessmentName(), assessmentsFiltersDTO.getUpdateDateFrom(), assessmentsFiltersDTO.getUpdateDateTo(), assessmentsFiltersDTO.getIuv(), assessmentsFiltersDTO.getDebtPositionTypeOrgCodes().stream().toList(), assessmentsFiltersDTO.getStatus(), 0, 1, Collections.emptyList())).thenReturn(pagedAssessmentsView);
    //when
    PagedAssessmentsView result = assessmentsClient.findPagedAssessmentsView(assessmentsFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedAssessmentsView.getContent(), result.getContent());
  }

  @Test
  void givenFiltersWhenFindPagedModelAssessmentsDetailThenReturnPagedModelAssessmentsDetail() {
    //given
    String accessToken = "accessToken";
    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = podamFactory.manufacturePojo(AssessmentsRowsDetailFiltersDTO.class);
    PagedModelAssessmentsDetail pagedModelAssessmentsDetail = podamFactory.manufacturePojo(PagedModelAssessmentsDetail.class);

    Mockito.when(classificationApisHolderMock.getAssessmentsDetailSearchControllerApi(accessToken)).thenReturn(assessmentsDetailSearchControllerApiMock);
    Mockito.when(assessmentsDetailSearchControllerApiMock.crudAssessmentsDetailsFindAssessmentsRowsDetail(assessmentsRowsDetailFiltersDTO.getAssessmentId(), assessmentsRowsDetailFiltersDTO.getIud(), assessmentsRowsDetailFiltersDTO.getIuv(), assessmentsRowsDetailFiltersDTO.getUpdateDateTimeIntervalFilter().getFrom(), assessmentsRowsDetailFiltersDTO.getUpdateDateTimeIntervalFilter().getTo(), assessmentsRowsDetailFiltersDTO.getPaymentDateTimeIntervalFilter().getFrom(), assessmentsRowsDetailFiltersDTO.getPaymentDateTimeIntervalFilter().getTo(), assessmentsRowsDetailFiltersDTO.getFiscalCode(), 0, 1, Collections.emptyList())).thenReturn(pagedModelAssessmentsDetail);
    //when
    PagedModelAssessmentsDetail result = assessmentsClient.findPagedModelAssessmentsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedModelAssessmentsDetail.getEmbedded().getAssessmentsDetails(), result.getEmbedded().getAssessmentsDetails());
  }

  @Test
  void givenIdWhenFindAssessmentsDetailThenReturnAssessmentsDetail() {
    //given
    String accessToken = "accessToken";
    Long assessmentDetailId = 1L;
    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);
    Mockito.when(classificationApisHolderMock.getAssessmentsDetailEntityControllerApi(accessToken)).thenReturn(assessmentsDetailEntityControllerApiMock);
    Mockito.when(assessmentsDetailEntityControllerApiMock.crudGetAssessmentsdetail(String.valueOf(assessmentDetailId))).thenReturn(assessmentsDetail);
    //when
    AssessmentsDetail result = assessmentsClient.findAssessmentsDetail(assessmentDetailId, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(assessmentsDetail, result);
  }

  @Test
  void givenIdWhenFindAssessmentsDetailThenReturnNull() {
    //given
    String accessToken = "accessToken";
    Long assessmentDetailId = 1L;
    Mockito.when(classificationApisHolderMock.getAssessmentsDetailEntityControllerApi(accessToken)).thenReturn(assessmentsDetailEntityControllerApiMock);
    Mockito.when(assessmentsDetailEntityControllerApiMock.crudGetAssessmentsdetail(String.valueOf(assessmentDetailId)))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    //when
    AssessmentsDetail result = assessmentsClient.findAssessmentsDetail(assessmentDetailId, accessToken);
    //then
    Assertions.assertNull(result);
  }

  @Test
  void givenOrganizationIdAssessmentNameAndDebtPositionTypeOrgCodeWhenCreateAssessmentThenReturnAssessment() {
    //given
    String accessToken = "accessToken";
    Long organizationId = 1L;
    String assessmentName = "testName";
    String debtPositionTypeOrgCode = "Code";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);

    Mockito.when(classificationApisHolderMock.getAssessmentsControllerApi(accessToken)).thenReturn(assessmentsControllerApiMock);
    Mockito.when(assessmentsControllerApiMock.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode)).thenReturn(assessments);
    //when
    Assessments result = assessmentsClient.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(assessments, result);
  }

  @Test
  void givenExistingAssessmentsWhenGetAssessmentsByIdThenReturnAssessments() {
    //given
    String accessToken = "accessToken";
    Long assessmentId = 1L;
    Assessments expectedResult = podamFactory.manufacturePojo(Assessments.class);
    Mockito.when(classificationApisHolderMock.getAssessmentsEntityControllerApi(accessToken)).thenReturn(assessmentsEntityControllerApiMock);
    Mockito.when(assessmentsEntityControllerApiMock.crudGetAssessments(String.valueOf(assessmentId))).thenReturn(expectedResult);
    //when
    Assessments result = assessmentsClient.getAssessmentsById(assessmentId, accessToken);
    //then
    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void givenNoAssessmentsWhenGetAssessmentsByIdThenReturnNull() {
    //given
    String accessToken = "accessToken";
    Long assessmentId = 1L;
    Mockito.when(classificationApisHolderMock.getAssessmentsEntityControllerApi(accessToken)).thenReturn(assessmentsEntityControllerApiMock);
    Mockito.when(assessmentsEntityControllerApiMock.crudGetAssessments(String.valueOf(assessmentId)))
            .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));
    //when
    Assessments result = assessmentsClient.getAssessmentsById(assessmentId, accessToken);
    //then
    Assertions.assertNull(result);
  }
}
