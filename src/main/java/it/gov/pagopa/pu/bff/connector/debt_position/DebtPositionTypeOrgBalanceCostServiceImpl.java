package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgBalanceCostClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeOrgBalanceCostServiceImpl implements DebtPositionTypeOrgBalanceCostService {
  private final DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient;

  public DebtPositionTypeOrgBalanceCostServiceImpl(DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient) {
    this.debtPositionTypeOrgBalanceCostClient = debtPositionTypeOrgBalanceCostClient;
  }

  @Override
  public CollectionModelDebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken) {
    return debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(debtPositionTypeOrgId, opYear, accessToken);
  }
}
