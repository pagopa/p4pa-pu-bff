package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.config.CacheConfig.Fields;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgWithCountClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = Fields.debtPositionTypeOrg)
public class DebtPositionTypeOrgServiceImpl implements DebtPositionTypeOrgService {

  private final DebtPositionTypeOrgClient debtPositionTypeOrgClient;
  private final DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClient;

  public DebtPositionTypeOrgServiceImpl(DebtPositionTypeOrgClient debtPositionTypeOrgClient, DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClient) {
    this.debtPositionTypeOrgClient = debtPositionTypeOrgClient;
    this.debtPositionTypeOrgWithCountClient = debtPositionTypeOrgWithCountClient;
  }

  @Override
  public CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);
  }

  @Override
  @Cacheable(key = "#debtPositionTypeOrgId", unless = "#result == null")
  public DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
  }

  @Override
  public PagedModelDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Pageable pageable, String accessToken) {
    return debtPositionTypeOrgWithCountClient.getDebtPositionTypeOrgWithCount(organizationId, code, description, pageable, accessToken);
  }

  @Override
  public CollectionModelDebtPositionTypeOrgCountByOrganizationId getDebtPositionTypeOrgCountByOrganizationId(
    List<Long> organizationIds, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgCountByOrganizationId(organizationIds, accessToken);
  }

  @Override
  public void deleteDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken) {
    debtPositionTypeOrgClient.deleteDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
  }

}
