package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypeOrgsApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DebtPositionTypeOrgController implements DebtPositionTypeOrgsApi {

  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;

  public DebtPositionTypeOrgController(
    DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService) {
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
  }

  @Override
  public ResponseEntity<List<DebtPositionTypeOrg>> getDebtPositionTypeOrgs(Long organizationId) {
    log.info("User requested getDebtPositionTypeOrgs having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgs(
      organizationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<PagedDebtPositionTypeOrgWithCount> getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Pageable pageable) {
    log.info("User requested getDebtPositionTypeOrgWithCount having organizationId {}", organizationId);
    return ResponseEntity.ok(debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgWithCount(
      organizationId, code, description, pageable, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
