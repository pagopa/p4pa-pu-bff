package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrgSubUnitOperatorsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.org_sub_unit_operators.OrgSubUnitOperatorsRetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class OrgSubUnitOperatorsController implements OrgSubUnitOperatorsApi {

  private final OrgSubUnitOperatorsRetrieverService orgSubUnitOperatorsRetrieverService;

  public OrgSubUnitOperatorsController(OrgSubUnitOperatorsRetrieverService orgSubUnitOperatorsRetrieverService) {
    this.orgSubUnitOperatorsRetrieverService = orgSubUnitOperatorsRetrieverService;
  }

  @Override
  public ResponseEntity<PagedOrgSubUnitOperators> getOrgSubUnitOperators(Long organizationId, String subUnitCode, Pageable pageable) {
    log.info("User requested getOrgSubUnitOperators having organizationId {} and subUnitCode {}", organizationId, subUnitCode);
    return ResponseEntity.ok(orgSubUnitOperatorsRetrieverService.getOrgSubUnitOperators(organizationId, subUnitCode, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> addOrgSubUnitsToOperator(Long organizationId, String mappedExternalUserId, List<String> orgSubUnitCodes) {
    log.info("Requested to add orgSubUnits {} to operator {} for organization {}", orgSubUnitCodes, mappedExternalUserId, organizationId);
    orgSubUnitOperatorsRetrieverService.addOrgSubUnitsToOperator(organizationId, mappedExternalUserId, orgSubUnitCodes, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }
}
