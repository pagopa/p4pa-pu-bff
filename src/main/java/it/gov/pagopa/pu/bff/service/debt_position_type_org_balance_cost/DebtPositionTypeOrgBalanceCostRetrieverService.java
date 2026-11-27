package it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;

import java.util.List;

public interface DebtPositionTypeOrgBalanceCostRetrieverService {
  List<DebtPositionTypeOrgBalanceCostDTO> getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYears(Long debtPositionTypeOrgId, List<String> opYears, String accessToken);

  DebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostByDptoIdAndYearAndType(Long organizationId, Long debtPositionTypeOrgId, String operatingYear, DebtPositionTypeOrgBalanceCostType type, UserInfo loggedUser, String accessToken);
}
