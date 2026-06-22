package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgBalanceCostClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtPositionTypeOrgBalanceCostServiceImpl implements DebtPositionTypeOrgBalanceCostService {
  private final DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient;

  public DebtPositionTypeOrgBalanceCostServiceImpl(DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient) {
    this.debtPositionTypeOrgBalanceCostClient = debtPositionTypeOrgBalanceCostClient;
  }

  @Override
  public CollectionModelDebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYears(Long debtPositionTypeOrgId, List<String> opYears, String accessToken) {
    return debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYears(debtPositionTypeOrgId, opYears, accessToken);
  }

  @Override
  public DebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(Long debtPositionTypeOrgId, String opYear, DebtPositionTypeOrgBalanceCostType type, String accessToken) {
    return debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(debtPositionTypeOrgId, opYear, type, accessToken);
  }
}
