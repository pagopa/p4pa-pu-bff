package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.AssessmentsDetailApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.assessments.AssessmentsDetailRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsDetail;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AssessmentsDetailController implements AssessmentsDetailApi {

  private final AssessmentsDetailRetrieverService assessmentsDetailRetrieverService;

    public AssessmentsDetailController(AssessmentsDetailRetrieverService assessmentsDetailRetrieverService) {
        this.assessmentsDetailRetrieverService = assessmentsDetailRetrieverService;
    }

    @Override
  public ResponseEntity<List<AssessmentsDetail>> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail) {
    log.info("User requested createAssessmentsDetail having organizationId {}, assessmentId {} and iuds {}", organizationId, assessmentId, createAssessmentsDetail.getIuds());
    return ResponseEntity.status(HttpStatus.CREATED).body(
            assessmentsDetailRetrieverService.createAssessmentsDetail(organizationId,assessmentId,createAssessmentsDetail, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken())
    );
  }
}
