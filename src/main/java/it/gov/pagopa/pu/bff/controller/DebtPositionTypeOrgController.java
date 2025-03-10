package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypeOrgsApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position_type_org.DebtPositionTypeOrgRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DebtPositionTypeOrgController implements DebtPositionTypeOrgsApi {

  private final DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService;

  public DebtPositionTypeOrgController(
    DebtPositionTypeOrgRetrieverService debtPositionTypeOrgRetrieverService) {
    this.debtPositionTypeOrgRetrieverService = debtPositionTypeOrgRetrieverService;
  }

  @Override
  public ResponseEntity<List<DebtPositionTypeOrg>> getDebtPositionTypeOrgs(Long organizationId) {
    return ResponseEntity.ok(debtPositionTypeOrgRetrieverService.getDebtPositionTypeOrgs(
      organizationId, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }

}
