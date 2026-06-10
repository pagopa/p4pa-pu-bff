package it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;

import java.util.List;

public interface DebtPositionTypeOrgBalanceCostRetrieverService {
  List<DebtPositionTypeOrgBalanceCostDTO> getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken);
}
