package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.AssessmentsApi;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.AssessmentsRowsDetailFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRowsDetail;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.assessments.AssessmentsRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import it.gov.pagopa.pu.classification.dto.generated.Assessments;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.List;

@Slf4j
@RestController
public class AssessmentsController implements AssessmentsApi {

  private final AssessmentsRetrieverService assessmentsRetrieverService;

  public AssessmentsController(AssessmentsRetrieverService assessmentsRetrieverService) {
    this.assessmentsRetrieverService = assessmentsRetrieverService;
  }

  @Override
  public ResponseEntity<PagedAssessmentsExtendedDTO> getPagedAssessmentsExtendedDTO(Long organizationId, String assessmentName, OffsetDateTime updateDateFrom, OffsetDateTime updateDateTo, String iuv, String debtPositionTypeOrgCode, AssessmentStatus status, Pageable pageable) {
    log.info("User requested getPagedAssessmentsExtendedDTO having organizationId {} and debtPositionTypeOrgCode {}", organizationId, debtPositionTypeOrgCode);
    return ResponseEntity.ok(assessmentsRetrieverService.getPagedAssessmentsExtendedDTO(
            AssessmentsFiltersDTO.builder()
                    .organizationId(organizationId)
                    .assessmentName(assessmentName)
                    .updateDateFrom(updateDateFrom)
                    .updateDateTo(updateDateTo)
                    .iuv(iuv)
                    .status(status).build(),
            debtPositionTypeOrgCode,
            pageable,
            SecurityUtils.getLoggedUser(),
            SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedAssessmentsRowsDetail> getPagedAssessmentsDetails(Long organizationId, Long assessmentId, String iuv, String iud, OffsetDateTime updateDateTimeFrom, OffsetDateTime updateDateTimeTo, OffsetDateTime paymentDateTimeFrom, OffsetDateTime paymentDateTimeTo, String fiscalCode, Pageable pageable) {
    log.info("User requested getPagedAssessmentsDetails having organizationId {} and assessmentId {}", organizationId, assessmentId);

    OffsetDateTimeIntervalFilter updateDateTimeIntervalFilter = new OffsetDateTimeIntervalFilter(updateDateTimeFrom, updateDateTimeTo);
    OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    return ResponseEntity.ok(assessmentsRetrieverService.getPagedAssessmentsRowsDetail(
      AssessmentsRowsDetailFiltersDTO.builder()
        .organizationId(organizationId)
        .assessmentId(assessmentId)
        .iud(iud)
        .iuv(iuv)
        .updateDateTimeIntervalFilter(updateDateTimeIntervalFilter)
        .paymentDateTimeIntervalFilter(paymentDateTimeIntervalFilter)
        .fiscalCode(fiscalCode)
        .build(),
      pageable,
      SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<AssessmentsDetail> getAssessmentsDetail(Long organizationId, Long assessmentId, Long assessmentDetailId) {
    log.info("User requested getAssessmentsDetail having organizationId {}, assessmentId {} and assessmentDetailId {}", organizationId, assessmentId, assessmentDetailId);
    return ResponseEntity.ok(assessmentsRetrieverService.getAssessmentsDetail(organizationId, assessmentId, assessmentDetailId, SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<String>> getOperatingYears() {
    log.info("User requested getOperatingYears");
    int currentYear = Year.now().getValue();
    List<String> years = List.of(
      String.valueOf(currentYear - 1),
      String.valueOf(currentYear),
      String.valueOf(currentYear + 1)
    );
    return ResponseEntity.ok(years);
  }

  @Override
  public ResponseEntity<Assessments> createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode) {
    log.info("User requested createAssessment having organizationId {}, assessmentName {} and debtPositionTypeOrgCode {}", organizationId, assessmentName, debtPositionTypeOrgCode);
    return ResponseEntity.ok(assessmentsRetrieverService.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
