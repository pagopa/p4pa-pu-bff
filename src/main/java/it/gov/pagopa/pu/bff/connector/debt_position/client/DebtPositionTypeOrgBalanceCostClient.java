package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class DebtPositionTypeOrgBalanceCostClient {
  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgBalanceCostClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelDebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYear(debtPositionTypeOrgId, opYear);
  }
}
