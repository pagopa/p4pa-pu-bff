package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.AssessmentsRegistryApi;
import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.AssessmentsRegistryDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.assessments_registry.AssessmentsRegistryRetrieverService;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistryStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AssessmentsRegistryController implements AssessmentsRegistryApi {

  private final AssessmentsRegistryRetrieverService assessmentsRegistryRetrieverService;

  public AssessmentsRegistryController(AssessmentsRegistryRetrieverService assessmentsRegistryRetrieverService) {
    this.assessmentsRegistryRetrieverService = assessmentsRegistryRetrieverService;
  }

  @Override
  public ResponseEntity<PagedAssessmentsRegistry> getAssessmentsRegistries(Long organizationId, String debtPositionTypeOrgCode, String sectionCode, String sectionDescription, String officeCode, String officeDescription, String assessmentCode, String assessmentDescription, String operatingYear, AssessmentsRegistryStatus status, Pageable pageable) {
    log.info("User requested getAssessmentsRegistries having organizationId {} and debtPositionTypeOrgCode {}", organizationId, debtPositionTypeOrgCode);
    return ResponseEntity.ok(assessmentsRegistryRetrieverService.getAssessmentsRegistries(
      AssessmentsRegistryFiltersDTO.builder()
        .organizationId(organizationId)
        .sectionCode(sectionCode)
        .sectionDescription(sectionDescription)
        .officeCode(officeCode)
        .officeDescription(officeDescription)
        .assessmentCode(assessmentCode)
        .assessmentDescription(assessmentDescription)
        .operatingYear(operatingYear)
        .status(status)
        .build(), debtPositionTypeOrgCode, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<AssessmentsRegistryDTO> getAssessmentsRegistry(Long organizationId, Long assessmentRegistryId) {
    log.info("User requested getAssessmentsRegistry having organizationId {} and assessmentRegistryId {}", organizationId, assessmentRegistryId);
    return ResponseEntity.ofNullable(assessmentsRegistryRetrieverService.getAssessmentsRegistry(
      organizationId, assessmentRegistryId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<AssessmentsRegistry> createAssessmentsRegistry(Long organizationId, AssessmentsRegistry body) {
    log.info("User requested createAssessmentsRegistry with organizationId {}", organizationId);
    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(assessmentsRegistryRetrieverService.createAssessmentsRegistry(organizationId,body,
        SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }
}
