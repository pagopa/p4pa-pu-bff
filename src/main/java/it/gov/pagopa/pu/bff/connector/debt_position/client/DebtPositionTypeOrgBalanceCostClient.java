package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


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

  public DebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(Long debtPositionTypeOrgId, String opYear, DebtPositionTypeOrgBalanceCostType type, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYearAndType(debtPositionTypeOrgId, opYear, type);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("DebtPositionTypeOrgBalanceCost with debtPositionTypeOrgId {}, opYear {} and type {} not found", debtPositionTypeOrgId, opYear, type);
      return null;
    }
  }
}
