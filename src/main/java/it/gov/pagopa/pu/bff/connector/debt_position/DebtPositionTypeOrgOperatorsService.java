package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;

public interface DebtPositionTypeOrgOperatorsService {
  CollectionModelDebtPositionTypeOrgOperators getDebtPositionTypeOrgOperators(Long debtPositionTypeOrgId, String accessToken);
  DebtPositionTypeOrgOperators findByDebtPositionTypeOrgIdAndOperatorExternalUserId(Long debtPositionTypeOrgId, String operatorExternalUserId, String accessToken);
}
