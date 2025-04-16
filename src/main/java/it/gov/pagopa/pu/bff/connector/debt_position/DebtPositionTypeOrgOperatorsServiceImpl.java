package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.config.CacheConfig.Fields;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = Fields.debtPositionTypeOrg)
public class DebtPositionTypeOrgOperatorsServiceImpl implements DebtPositionTypeOrgOperatorsService {

  private final DebtPositionTypeOrgOperatorsClient debtPositionTypeOrgOperatorsClient;

  public DebtPositionTypeOrgOperatorsServiceImpl(
    DebtPositionTypeOrgOperatorsClient debtPositionTypeOrgOperatorsClient) {
    this.debtPositionTypeOrgOperatorsClient = debtPositionTypeOrgOperatorsClient;
  }

  @Override
  public CollectionModelDebtPositionTypeOrgOperators getDebtPositionTypeOrgOperators(Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionTypeOrgOperatorsClient.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);
  }
}
