package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;

public interface DebtPositionTypeOrgBalanceCostService {
  CollectionModelDebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken);
}
