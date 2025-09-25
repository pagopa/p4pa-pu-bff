package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.config.CacheConfig.Fields;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgSearchClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgWithCountClient;
import it.gov.pagopa.pu.bff.dto.OperatorDetailsFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgWithCount;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
import java.util.List;
import java.util.Set;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  public CollectionModelDebtPositionTypeOrg getDebtPositionTypeOrgs(Long organizationId, Boolean flagActive, String operatorExternalUserId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgs(organizationId, operatorExternalUserId, flagActive, accessToken);
  }

  @Override
  @Cacheable(key = "#debtPositionTypeOrgId", unless = "#result == null")
  public DebtPositionTypeOrg getDebtPositionTypeOrg(Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrg(debtPositionTypeOrgId, accessToken);
  }

  @Override
  public PagedModelDebtPositionTypeOrgWithCount getDebtPositionTypeOrgWithCount(Long organizationId, String code, String description, Boolean flagActive, Pageable pageable, String accessToken) {
    return debtPositionTypeOrgWithCountClient.getDebtPositionTypeOrgWithCount(organizationId, code, description, flagActive, pageable, accessToken);
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
    return debtPositionTypeOrgClient.saveDebtPositionTypeOrg(saveDebtPositionTypeOrg, accessToken);
  }

  @Override
  public DebtPositionTypeOrg findDebtPositionTypeOrg(Long organizationId, String debtPositionTypeOrgCode, String mappedExternalUserId, String accessToken) {
    return debtPositionTypeOrgSearchClient.findDebtPositionTypeOrg(organizationId, debtPositionTypeOrgCode, mappedExternalUserId, accessToken);
  }

  @Override
  public List<DebtPositionTypeOrg> findDebtPositionTypeOrgByOrganizationIdAndIuds(Long organizationId, Set<String> iuds, String accessToken) {
    return debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgByOrganizationIdAndIuds(organizationId, iuds, accessToken);
  }

  @Override
  @CacheEvict(key = "#debtPositionTypeOrgId")
  public void updateFlagActiveDebtPositionTypeOrg(Long debtPositionTypeOrgId, Boolean flagActive, String accessToken) {
    debtPositionTypeOrgClient.updateFlagActiveDebtPositionTypeOrg(debtPositionTypeOrgId, flagActive, accessToken);
  }

  @Override
  public Long countByOrgSilServiceId(Long orgSilServiceId, String accessToken) {
    return debtPositionTypeOrgSearchClient.countByOrgSilServiceId(orgSilServiceId, accessToken);
  }

  @Override
  public PagedModelDebtPositionTypeOrg findPagedDebtPositionTypeOrg(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, String accessToken) {
    return debtPositionTypeOrgSearchClient.findPagedDebtPositionTypeOrg(operatorDetailsFiltersDTO, pageable, accessToken);
  }

  @Override
  public PagedModelDebtPositionTypeOrg findDebtPositionTypeOrgNotEnabledForOperator(OperatorDetailsFiltersDTO operatorDetailsFiltersDTO, Pageable pageable, String accessToken) {
    return debtPositionTypeOrgSearchClient.findDebtPositionTypeOrgNotEnabledForOperator(operatorDetailsFiltersDTO, pageable, accessToken);
  }

  @Override
  public CollectionModelDebtPositionTypeOrg getByDebtPositionTypeOrgIdIn(Set<Long> debtPositionTypeOrgIds, String accessToken) {
    return debtPositionTypeOrgClient.getByDebtPositionTypeOrgIdIn(debtPositionTypeOrgIds, accessToken);
  }
}
