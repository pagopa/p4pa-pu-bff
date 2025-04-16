package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DebtPositionTypeOrgOperatorsClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgOperatorsClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelDebtPositionTypeOrgOperators getDebtPositionTypeOrgOperators(Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgId(debtPositionTypeOrgId);
  }

}

