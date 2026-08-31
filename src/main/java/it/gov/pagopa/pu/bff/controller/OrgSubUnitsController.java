package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.OrgSubUnitsApi;
import it.gov.pagopa.pu.bff.dto.PagedOrgSubUnitFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnit;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.org_sub_unit.OrgSubUnitRetrieverService;
import it.gov.pagopa.pu.organization.dto.generated.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrgSubUnitsController implements OrgSubUnitsApi {
  private final OrgSubUnitRetrieverService subUnitRetrieverService;

  public OrgSubUnitsController(OrgSubUnitRetrieverService subUnitRetrieverService) {
    this.subUnitRetrieverService = subUnitRetrieverService;
  }

  @Override
  public ResponseEntity<OrgSubUnit> createOrgSubUnit(Long organizationId, OrgSubUnitRequestBody body) {
    log.info("User requested createOrgSubUnit having organizationId {} and orgSubUnitCode {}", organizationId, body.getSubUnitCode());
    return ResponseEntity.ok(subUnitRetrieverService.createOrgSubUnit(organizationId, body, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> deleteOrgSubUnitById(Long organizationId, String subUnitCode) {
    log.info("User requested deleteOrgSubUnitById having organizationId {} and subUnitCode {}", organizationId, subUnitCode);
    subUnitRetrieverService.deleteOrgSubUnit(organizationId, subUnitCode, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<OrgSubUnit> getOrgSubUnitById(Long organizationId, String subUnitCode) {
    log.info("User requested getOrgSubUnitById having subUnitCode {}", subUnitCode);
    return ResponseEntity.ok(subUnitRetrieverService.getOrgSubUnitById(organizationId, subUnitCode, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<OrgSubUnit> updateOrgSubUnit(Long organizationId, String subUnitCode, OrgSubUnitRequestBody body) {
    log.info("User requested updateOrgSubUnit having organizationId {} and subUnitCode {}", organizationId, subUnitCode);
    return ResponseEntity.ok(subUnitRetrieverService.updateOrgSubUnit(organizationId, subUnitCode, body, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> updateOrgSubUnitStatus(Long organizationId, String subUnitCode, OrgSubUnitStatus status) {
    log.info("User requested updating status to {} for OrgSubUnit [organizationId={}, subUnitCode={}]", status, organizationId, subUnitCode);
    subUnitRetrieverService.updateOrgSubUnitStatus(organizationId, subUnitCode, status, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<PagedOrgSubUnit> getPagedOrgSubUnits(Long organizationId, String mappedExternalUserId, String subUnitCode, OrgSubUnitStatus status, SubUnitType subUnitType, Pageable pageable) {
    log.info("User requested getOrgSubUnits having organizationId {}", organizationId);
    PagedOrgSubUnitFiltersDTO filters = new PagedOrgSubUnitFiltersDTO(
      organizationId,
      mappedExternalUserId,
      subUnitCode,
      status,
      subUnitType
    );
    return ResponseEntity.ok(subUnitRetrieverService.getPagedOrgSubUnits(filters, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
