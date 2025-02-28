package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.config.CacheConfig.Fields;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = Fields.debtPositionTypeOrg)
public class DebtPositionTypeOrgServiceImpl implements DebtPositionTypeOrgService {

  private final DebtPositionTypeOrgClient client;

  public DebtPositionTypeOrgServiceImpl(DebtPositionTypeOrgClient client) {
    this.client = client;
  }

  @Override
  public CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(
    Long organizationId,
    String operatorExternalUserId, String accessToken) {
    return client.getDebtPositionTypeOrgs(organizationId,operatorExternalUserId,accessToken);
  }

  @Override
  @Cacheable(key = "#debtPositionTypeOrgId", unless="#result == null")
  public DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken) {
    return client.getDebtPositionTypeOrg(debtPositionTypeOrgId,accessToken);
  }
}
