package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrgSubUnitOperatorsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.org_sub_unit_operators.OrgSubUnitOperatorsRetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
