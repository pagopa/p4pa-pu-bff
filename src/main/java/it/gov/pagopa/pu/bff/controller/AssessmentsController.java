package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.AssessmentsApi;
import it.gov.pagopa.pu.bff.dto.AssessmentsFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsExtendedDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.assessments.AssessmentsRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

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

}
