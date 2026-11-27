package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;

import java.util.List;

public interface DebtPositionTypeOrgBalanceCostService {
  CollectionModelDebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYears(Long debtPositionTypeOrgId, List<String> opYears, String accessToken);

  DebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(Long debtPositionTypeOrgId, String opYear, DebtPositionTypeOrgBalanceCostType type, String accessToken);
}
