package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.config.CacheConfig.Fields;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsApiClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsDptoCountViewClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeOrgOperatorsSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@CacheConfig(cacheNames = Fields.debtPositionTypeOrg)
public class DebtPositionTypeOrgOperatorsServiceImpl implements DebtPositionTypeOrgOperatorsService {

  private final DebtPositionTypeOrgOperatorsSearchClient debtPositionTypeOrgOperatorsSearchClient;
  private final DebtPositionTypeOrgOperatorsDptoCountViewClient debtPositionTypeOrgOperatorsDptoCountViewClient;
  private final DebtPositionTypeOrgOperatorsApiClient debtPositionTypeOrgOperatorsApiClient;

  public DebtPositionTypeOrgOperatorsServiceImpl(DebtPositionTypeOrgOperatorsSearchClient debtPositionTypeOrgOperatorsSearchClient,
                                                 DebtPositionTypeOrgOperatorsDptoCountViewClient debtPositionTypeOrgOperatorsDptoCountViewClient,
                                                 DebtPositionTypeOrgOperatorsApiClient debtPositionTypeOrgOperatorsApiClient) {
      this.debtPositionTypeOrgOperatorsSearchClient = debtPositionTypeOrgOperatorsSearchClient;
      this.debtPositionTypeOrgOperatorsDptoCountViewClient = debtPositionTypeOrgOperatorsDptoCountViewClient;
      this.debtPositionTypeOrgOperatorsApiClient = debtPositionTypeOrgOperatorsApiClient;
  }

  @Override
  public CollectionModelDebtPositionTypeOrgOperators getDebtPositionTypeOrgOperators(Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionTypeOrgOperatorsSearchClient.getDebtPositionTypeOrgOperators(debtPositionTypeOrgId, accessToken);
  }

  @Override
  public DebtPositionTypeOrgOperators findByDebtPositionTypeOrgIdAndOperatorExternalUserId(Long debtPositionTypeOrgId, String operatorExternalUserId, String accessToken) {
    return debtPositionTypeOrgOperatorsSearchClient.findByDebtPositionTypeOrgIdAndOperatorExternalUserId(debtPositionTypeOrgId,operatorExternalUserId, accessToken);
  }

  @Override
  public List<DebtPositionTypeOrgOperatorsDptoCountView> findByOrganizationIdAndOperatorExternalUserIds(Long organizationId, Set<String> operatorIds, String accessToken) {
    return debtPositionTypeOrgOperatorsDptoCountViewClient.findByOrganizationIdAndOperatorExternalUserIds(organizationId,operatorIds, accessToken);
  }

  @Override
  public int deleteOperators(Long debtPositionTypeOrgId, Set<String> externalOperatorUserIds, String accessToken) {
    return debtPositionTypeOrgOperatorsApiClient.deleteOperators(debtPositionTypeOrgId, externalOperatorUserIds, accessToken);
  }

  @Override
  public void saveDebtPositionTypeOrgOperatorsForOperator(String operatorExternalUserId, Set<Long> debtPositionTypeOrgIds, String accessToken) {
    debtPositionTypeOrgOperatorsApiClient.saveDebtPositionTypeOrgOperatorsForOperator(operatorExternalUserId, debtPositionTypeOrgIds, accessToken);
  }
}
