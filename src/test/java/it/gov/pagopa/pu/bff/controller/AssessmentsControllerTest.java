package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.assessments.AssessmentsRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

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

    AssessmentsFiltersDTO assessmentsFiltersDTO = new AssessmentsFiltersDTO(organizationId, assessmentsName, offsetDateTime, offsetDateTime.plusDays(1L), iuv, null,  AssessmentStatus.NEW);

    PagedAssessmentsExtendedDTO pagedAssessmentsExtendedDTO = new PagedAssessmentsExtendedDTO();

    Mockito.when(assessmentsRetrieverServiceMock.getPagedAssessmentsExtendedDTO(assessmentsFiltersDTO, debtPositionTypeOrgCode, Pageable.ofSize(1), loggedUser, accessToken)).thenReturn(pagedAssessmentsExtendedDTO);
    //when
    ResponseEntity<PagedAssessmentsExtendedDTO> result = assessmentsController.getPagedAssessmentsExtendedDTO(organizationId, assessmentsName, offsetDateTime, offsetDateTime.plusDays(1L), iuv, debtPositionTypeOrgCode, AssessmentStatus.NEW, Pageable.ofSize(1));
    //then
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedAssessmentsExtendedDTO, result.getBody());
  }

  @Test
  void givenParametersWhenGetPagedAssessmentDetailThenOk() {
    //given
    Long organizationId = 1L;
    Long assessmentId = 1L;
    String iuv = "iuv";
    String iud = "iud";
    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    OffsetDateTimeIntervalFilter offsetDateTimeIntervalFilter = new OffsetDateTimeIntervalFilter(offsetDateTime, offsetDateTime);
    String fiscalCode = "fiscalCode";

    AssessmentsRowsDetailFiltersDTO assessmentsRowsDetailFiltersDTO = new AssessmentsRowsDetailFiltersDTO(organizationId, assessmentId, iud, iuv, offsetDateTimeIntervalFilter, offsetDateTimeIntervalFilter, fiscalCode);
    PagedAssessmentsRowsDetail pagedAssessmentsRowsDetail = new PagedAssessmentsRowsDetail();
    Mockito.when(assessmentsRetrieverServiceMock.getPagedAssessmentsRowsDetail(assessmentsRowsDetailFiltersDTO, Pageable.ofSize(1),loggedUser, accessToken)).thenReturn(pagedAssessmentsRowsDetail);
    //when
    ResponseEntity<PagedAssessmentsRowsDetail> result = assessmentsController.getPagedAssessmentsRows(organizationId, assessmentId, iuv, iud, offsetDateTime, offsetDateTime, offsetDateTime, offsetDateTime, fiscalCode, Pageable.ofSize(1));
    //then
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result);
    Assertions.assertEquals(pagedAssessmentsRowsDetail, result.getBody());
  }
}
