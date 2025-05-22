package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

  public DebtPositionTypeOrgOperators findByDebtPositionTypeOrgIdAndOperatorExternalUserId(Long debtPositionTypeOrgId,String operatorExternalUserId, String accessToken) {
    try{  return debtPositionApisHolder.getDebtPositionTypeOrgOperatorsSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgOperatorsFindByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId,operatorExternalUserId);
    } catch (HttpClientErrorException.NotFound e) {
      log.info("DebtPositionTypeOrgOperator having DebtPositionTypeOrgId {} and operatorExternalUserId {} not found", debtPositionTypeOrgId, operatorExternalUserId);
      return null;
    }
  }
}

