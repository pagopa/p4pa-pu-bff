package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.*;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.assessments.AssessmentsRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AssessmentsControllerTest {

  @Mock
  private AssessmentsRetrieverService assessmentsRetrieverServiceMock;

  private AssessmentsController assessmentsController;

  private final String accessToken = "fakeAccessToken";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);
    assessmentsController = new AssessmentsController(assessmentsRetrieverServiceMock);
  }

  @Test
  void givenParametersWhenGetPagedAssessmentsExtendedDTOThenOK() {
    //given
    Long organizationId = 1L;
    String assessmentsName = "assessmentsName";
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    String iuv = "iuv";
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";

    AssessmentsFiltersDTO assessmentsFiltersDTO = new AssessmentsFiltersDTO(organizationId, assessmentsName, offsetDateTime, offsetDateTime.plusDays(1L), iuv, null, AssessmentStatus.ACTIVE);

    PagedAssessmentsExtendedDTO pagedAssessmentsExtendedDTO = new PagedAssessmentsExtendedDTO();

    Mockito.when(assessmentsRetrieverServiceMock.getPagedAssessmentsExtendedDTO(assessmentsFiltersDTO, debtPositionTypeOrgCode, Pageable.ofSize(1), loggedUser, accessToken)).thenReturn(pagedAssessmentsExtendedDTO);
    //when
    ResponseEntity<PagedAssessmentsExtendedDTO> result = assessmentsController.getPagedAssessmentsExtendedDTO(organizationId, assessmentsName, offsetDateTime, offsetDateTime.plusDays(1L), iuv, debtPositionTypeOrgCode, AssessmentStatus.ACTIVE, Pageable.ofSize(1));
    //then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result);
    assertEquals(pagedAssessmentsExtendedDTO, result.getBody());
  }

  @Test
  void givenParametersWhenGetPagedAssessmentsDetailsThenOk() {
    //given
    Long organizationId = 1L;
    Long assessmentId = 1L;
    String iuv = "iuv";
    String iud = "iud";
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter offsetDateTimeIntervalFilter = new OffsetDateTimeIntervalFilter(offsetDateTime, offsetDateTime);
    String fiscalCode = "fiscalCode";
    LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(localDateTime , localDateTime );
    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = new AssessmentsRowsDetailFiltersDTO(organizationId, assessmentId, iud, iuv, localDateTimeIntervalFilter, offsetDateTimeIntervalFilter, fiscalCode);
    AssessmentsRowsDetail pagedAssessmentsRowsDetail = new AssessmentsRowsDetail();
    Mockito.when(assessmentsRetrieverServiceMock.getPagedAssessmentsRowsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1), loggedUser, accessToken)).thenReturn(pagedAssessmentsRowsDetail);
    //when
    ResponseEntity<AssessmentsRowsDetail> result = assessmentsController.getPagedAssessmentsDetails(organizationId, assessmentId, iuv, iud, offsetDateTime, offsetDateTime, offsetDateTime, offsetDateTime, fiscalCode, Pageable.ofSize(1));
    //then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result);
    assertEquals(pagedAssessmentsRowsDetail, result.getBody());
  }

  @Test
  void givenParametersWhenGetAssessmentDetailThenOk() {
    //given
    Long organizationId = 1L;
    Long assessmentId = 1L;
    Long assessmentDetailId = 1L;
    AssessmentsDetail assessmentsDetail = new AssessmentsDetail();

    Mockito.when(assessmentsRetrieverServiceMock.getAssessmentsDetail(organizationId, assessmentId, assessmentDetailId, loggedUser, accessToken)).thenReturn(assessmentsDetail);
    //when
    ResponseEntity<AssessmentsDetail> result = assessmentsController.getAssessmentsDetail(organizationId, assessmentId, assessmentDetailId);
    //then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result);
    assertEquals(assessmentsDetail, result.getBody());
  }

  @Test
  void whenGetOperatingYearsThenOk() {
    int currentYear = Year.now().getValue();
    List<String> expectedYears = List.of(
      String.valueOf(currentYear - 1),
      String.valueOf(currentYear),
      String.valueOf(currentYear + 1));

    ResponseEntity<List<String>> result = assessmentsController.getOperatingYears();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result);
    assertEquals(expectedYears, result.getBody());
  }

  @Test
  void whenCreateAssessmentThenOk() {
    //given
    Long organizationId = 1L;
    String assessmentsName = "assessmentsName";
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    Assessments assessments = new Assessments();
    Mockito.when(assessmentsRetrieverServiceMock.createAssessment(organizationId, assessmentsName, debtPositionTypeOrgCode,loggedUser, accessToken)).thenReturn(assessments);
    //when
    ResponseEntity<Assessments> result = assessmentsController.createAssessment(organizationId, assessmentsName, debtPositionTypeOrgCode);
    //then
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result);
    assertEquals(assessments, result.getBody());
  }

}
