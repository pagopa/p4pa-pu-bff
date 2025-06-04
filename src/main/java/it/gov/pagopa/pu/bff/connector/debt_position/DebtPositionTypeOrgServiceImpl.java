package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.config.CacheConfig.Fields;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgSearchClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgWithCountClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = Fields.debtPositionTypeOrg)
public class DebtPositionTypeOrgServiceImpl implements DebtPositionTypeOrgService {

  private final DebtPositionTypeOrgClient debtPositionTypeOrgClient;
  private final DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClient;
  private final DebtPositionTypeOrgSearchClient debtPositionTypeOrgSearchClient;

  public DebtPositionTypeOrgServiceImpl(DebtPositionTypeOrgClient debtPositionTypeOrgClient, DebtPositionTypeOrgWithCountClient debtPositionTypeOrgWithCountClient, DebtPositionTypeOrgSearchClient debtPositionTypeOrgSearchClient) {
    this.debtPositionTypeOrgClient = debtPositionTypeOrgClient;
    this.debtPositionTypeOrgWithCountClient = debtPositionTypeOrgWithCountClient;
    this.debtPositionTypeOrgSearchClient = debtPositionTypeOrgSearchClient;
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

  @Override
  public PagedModelDebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeId(Long debtPositionTypeId, Pageable pageable, String accessToken) {
    return debtPositionTypeOrgSearchClient.getDebtPositionTypeOrgByDebtPositionTypeId(debtPositionTypeId, pageable, accessToken);
  }

  @Override
  @CacheEvict(key = "#saveDebtPositionTypeOrg.debtPositionTypeOrg.debtPositionTypeOrgId",
          condition = "#saveDebtPositionTypeOrg!=null && #saveDebtPositionTypeOrg.debtPositionTypeOrg!=null && #saveDebtPositionTypeOrg.debtPositionTypeOrg.debtPositionTypeOrgId!=null")
  public DebtPositionTypeOrg saveDebtPositionTypeOrg(
    SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrg, String accessToken) {
    return debtPositionTypeOrgClient.saveDebtPositionTypeOrg(saveDebtPositionTypeOrg,accessToken);
  }
}
