package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.TaxonomyApi;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.taxonomy.TaxonomyRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class TaxonomyController implements TaxonomyApi {

  private final TaxonomyRetrieverService taxonomyRetrieverService;

  public TaxonomyController(TaxonomyRetrieverService taxonomyRetrieverService) {
    this.taxonomyRetrieverService = taxonomyRetrieverService;
  }

  @Override
  public ResponseEntity<Taxonomy> getTaxonomyDetail(Long taxonomyId) {
    log.info("User requested getTaxonomyDetail having taxonomyId {}", taxonomyId);
    return ResponseEntity.ofNullable(taxonomyRetrieverService.getTaxonomyDetail(
      taxonomyId, SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<TaxonomyCollectionReasonDTO>> getCollectionReason(String organizationType, String macroAreaCode, String serviceTypeCode) {
    log.info("User requested getCollectionReason");
    return new ResponseEntity<>(taxonomyRetrieverService.getCollectionReason(
      organizationType, macroAreaCode, serviceTypeCode, SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<TaxonomyMacroAreaCodeDTO>> getMacroArea(String organizationType) {
    log.info("User requested getMacroArea");
    return new ResponseEntity<>(taxonomyRetrieverService.getMacroArea(
      organizationType, SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<TaxonomyOrganizationTypeDTO>> getOrganizationTypes() {
    log.info("User requested getOrganizationTypes");
    return new ResponseEntity<>(taxonomyRetrieverService.getOrganizationTypes(
      SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<TaxonomyServiceTypeCodeDTO>> getServiceType(String organizationType, String macroAreaCode) {
    log.info("User requested getServiceType");
    return new ResponseEntity<>(taxonomyRetrieverService.getServiceType(
      organizationType, macroAreaCode, SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<PagedTaxonomy> getTaxonomies(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason, Pageable pageable) {
    log.info("User requested getTaxonomies");
    return ResponseEntity.ok(taxonomyRetrieverService.getTaxonomies(
      organizationType, macroAreaCode, serviceTypeCode, collectionReason, pageable, SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<List<TaxonomyCodeDTO>> getTaxonomyCode(String organizationType, String macroAreaCode, String serviceTypeCode, String collectionReason) {
    log.info("User requested getTaxonomyCode");
    return new ResponseEntity<>(taxonomyRetrieverService.getTaxonomyCode(
      organizationType, macroAreaCode, serviceTypeCode, collectionReason, SecurityUtils.getAccessToken()), HttpStatus.OK);
  }
}
