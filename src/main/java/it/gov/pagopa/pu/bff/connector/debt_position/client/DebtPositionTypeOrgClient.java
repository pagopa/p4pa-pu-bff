package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DebtPositionTypeOrgClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(String.valueOf(organizationId), operatorExternalUserId);
    } catch (Exception e) {
      log.error("Unexpected error retrieving DebtPositionTypeOrg for organizationId {}: {}", organizationId, e.getMessage(), e);
      throw e;
    }
  }

}

