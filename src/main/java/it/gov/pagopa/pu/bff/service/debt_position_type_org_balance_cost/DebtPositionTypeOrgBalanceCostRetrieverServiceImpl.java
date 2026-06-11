package it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost;

import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgBalanceCostMapper;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtPositionTypeOrgBalanceCostRetrieverServiceImpl implements DebtPositionTypeOrgBalanceCostRetrieverService {
  private final DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService;
  private final DebtPositionTypeOrgBalanceCostMapper debtPositionTypeOrgBalanceCostMapper;

  public DebtPositionTypeOrgBalanceCostRetrieverServiceImpl(
    DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService,
    DebtPositionTypeOrgBalanceCostMapper debtPositionTypeOrgBalanceCostMapper
  ) {
    this.debtPositionTypeOrgBalanceCostService = debtPositionTypeOrgBalanceCostService;
    this.debtPositionTypeOrgBalanceCostMapper = debtPositionTypeOrgBalanceCostMapper;
  }

  @Override
  public List<DebtPositionTypeOrgBalanceCostDTO> getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken) {
    return debtPositionTypeOrgBalanceCostService
      .getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(debtPositionTypeOrgId, opYear, accessToken)
      .getEmbedded()
      .getDebtPositionTypeOrgBalanceCosts()
      .stream()
      .map(debtPositionTypeOrgBalanceCostMapper::map)
      .toList();
  }

  @Override
  public DebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(Long debtPositionTypeOrgId, String opYear, DebtPositionTypeOrgBalanceCostType type, String accessToken) {
    return debtPositionTypeOrgBalanceCostService.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearAndType(debtPositionTypeOrgId, opYear, type, accessToken);
  }
}
