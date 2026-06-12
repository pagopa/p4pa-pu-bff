package it.gov.pagopa.pu.bff.service.debt_position_type_org_balance_cost;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeOrgBalanceCostMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtPositionTypeOrgBalanceCostRetrieverServiceImpl implements DebtPositionTypeOrgBalanceCostRetrieverService {

  private final DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService;
  private final DebtPositionTypeOrgBalanceCostMapper debtPositionTypeOrgBalanceCostMapper;
  private final AuthorizationService authorizationService;

  public DebtPositionTypeOrgBalanceCostRetrieverServiceImpl(
    DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService,
    DebtPositionTypeOrgBalanceCostMapper debtPositionTypeOrgBalanceCostMapper, AuthorizationService authorizationService
  ) {
    this.debtPositionTypeOrgBalanceCostService = debtPositionTypeOrgBalanceCostService;
    this.debtPositionTypeOrgBalanceCostMapper = debtPositionTypeOrgBalanceCostMapper;
    this.authorizationService = authorizationService;
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
  public DebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostByDptoIdAndYearAndType(Long organizationId, Long debtPositionTypeOrgId, String operatingYear, DebtPositionTypeOrgBalanceCostType type, UserInfo loggedUser, String accessToken) {
    authorizationService.validateAdminRole(organizationId, loggedUser);
    DebtPositionTypeOrgBalanceCost debtPositionTypeOrgBalanceCost = debtPositionTypeOrgBalanceCostService.getDebtPositionTypeOrgBalanceCostByDptoIdAndOpYearAndType(debtPositionTypeOrgId, operatingYear, type, accessToken);
    if (debtPositionTypeOrgBalanceCost == null) {
      throw new ResourceNotFoundException("DEBT_POSITION_TYPE_ORG_BALANCE_COST_NOT_FOUND", "DebtPositionTypeOrgBalanceCost with debtPositionTypeOrgId %d, opYear %s and type %s not found".formatted(debtPositionTypeOrgId, operatingYear, type));
    }
    return debtPositionTypeOrgBalanceCost;
  }
}
