package it.gov.pagopa.pu.bff.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.gov.pagopa.pu.bff.controller.generated.TaxonomyApi;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.taxonomy.TaxonomyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TaxonomyController implements TaxonomyApi {

  private final TaxonomyService taxonomyService;
  public TaxonomyController(TaxonomyService taxonomyService){
    this.taxonomyService = taxonomyService;
  }

  @Override
  public ResponseEntity<CollectionModelTaxonomyCollectionReasonDTO> getCollectionReason (
    String organizationType, String macroAreaCode, String serviceTypeCode) {
    return new ResponseEntity<>(taxonomyService.getCollectionReason(
      organizationType,
      macroAreaCode, serviceTypeCode, SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CollectionModelTaxonomyMacroAreaCodeDTO> getMacroArea (
    String organizationType) {
    return new ResponseEntity<>(taxonomyService.getMacroArea(organizationType,SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CollectionModelTaxonomyOrganizationTypeDTO> getOrganizationTypes () {
    return new ResponseEntity<>(taxonomyService.getOrganizationTypes(SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CollectionModelTaxonomyServiceTypeCodeDTO> getServiceType (
    String organizationType, String macroAreaCode) {
    return new ResponseEntity<>(taxonomyService.getServiceType(organizationType, macroAreaCode,SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CollectionModelTaxonomyCodeDTO> getTaxonomyCode (
    String organizationType,
    String macroAreaCode, String serviceTypeCode, String collectionReason) {
    return new ResponseEntity<>(taxonomyService.getTaxonomyCode(organizationType, macroAreaCode,
      serviceTypeCode,
      collectionReason,SecurityUtils.getAccessToken()), HttpStatus.OK);
  }
}
