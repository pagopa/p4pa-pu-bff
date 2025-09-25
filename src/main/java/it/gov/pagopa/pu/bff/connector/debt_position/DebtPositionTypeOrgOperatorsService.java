package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperatorsDptoCountView;

import java.util.List;
import java.util.Set;

public interface DebtPositionTypeOrgOperatorsService {
  CollectionModelDebtPositionTypeOrgOperators getDebtPositionTypeOrgOperators(Long debtPositionTypeOrgId, String accessToken);
  DebtPositionTypeOrgOperators findByDebtPositionTypeOrgIdAndOperatorExternalUserId(Long debtPositionTypeOrgId, String operatorExternalUserId, String accessToken);
  List<DebtPositionTypeOrgOperatorsDptoCountView> findByOrganizationIdAndOperatorExternalUserIds(Long organizationId, Set<String> operatorIds, String accessToken);
  int deleteOperators(Long debtPositionTypeOrgId, Set<String> externalOperatorUserIds, String accessToken);
  void saveDebtPositionTypeOrgOperatorsForOperator(String operatorExternalUserId, Set<Long> debtPositionTypeOrgIds, String accessToken);
}
