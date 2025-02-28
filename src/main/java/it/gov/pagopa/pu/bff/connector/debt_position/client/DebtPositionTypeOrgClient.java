package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class DebtPositionTypeOrgClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(String.valueOf(organizationId), operatorExternalUserId);
  }

  public DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId,
    String accessToken) {
    try{
      return debtPositionApisHolder.getDebtPositionTypeOrgEntityControllerApi(accessToken)
        .crudGetDebtpositiontypeorg(String.valueOf(debtPositionTypeOrgId));
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("DebtPositionTypeOrg with debtPositionTypeOrgId {} not found", debtPositionTypeOrgId);
      return null;
    }
  }
}

