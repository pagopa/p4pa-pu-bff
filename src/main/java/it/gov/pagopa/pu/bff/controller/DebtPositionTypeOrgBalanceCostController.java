package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypeOrgBalanceCostApi;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost.DebtPositionTypeOrgBalanceCostRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DebtPositionTypeOrgBalanceCostController implements DebtPositionTypeOrgBalanceCostApi {

  private final DebtPositionTypeOrgBalanceCostRetrieverService debtPositionTypeOrgBalanceCostRetrieverService;

  public DebtPositionTypeOrgBalanceCostController(DebtPositionTypeOrgBalanceCostRetrieverService debtPositionTypeOrgBalanceCostRetrieverService) {
    this.debtPositionTypeOrgBalanceCostRetrieverService = debtPositionTypeOrgBalanceCostRetrieverService;
  }

  @Override
  public ResponseEntity<DebtPositionTypeOrgBalanceCost> getDebtPositionTypeOrgBalanceCostByDebtPositionTypeOrgIdAndYearAndType(Long organizationId, Long debtPositionTypeOrgId, String operatingYear, DebtPositionTypeOrgBalanceCostType type){
    log.info("User requested getDebtPositionTypeOrgBalanceCost having organizationId {}, debtPositionTypeOrgId {}, operatingYear {} and type {}", organizationId, debtPositionTypeOrgId, operatingYear, type);
    return ResponseEntity.ok(debtPositionTypeOrgBalanceCostRetrieverService.getDebtPositionTypeOrgBalanceCostByDptoIdAndYearAndType(organizationId, debtPositionTypeOrgId, operatingYear, type, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
  }
}
