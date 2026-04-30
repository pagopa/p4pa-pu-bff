package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionSearchClient;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtPositionServiceImpl implements DebtPositionService {

  private final DebtPositionClient client;
  private final DebtPositionSearchClient debtPositionSearchClient;

  public DebtPositionServiceImpl(DebtPositionClient client,
    DebtPositionSearchClient debtPositionSearchClient) {
    this.client = client;
    this.debtPositionSearchClient = debtPositionSearchClient;
  }

  @Override
  public DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, boolean massive, String accessToken) {
    return client.createDebtPosition(debtPositionDTO, massive, accessToken);
  }

  @Override
  public PagedModelDebtPositionView getDebtPositionViews(
    DebtPositionViewFiltersDTO filtersDTO, List<DebtPositionOrigin> debtPositionOrigins, String operatorExternalId,
    Pageable pageable, String accessToken) {
    return client.getDebtPositionViews(filtersDTO, debtPositionOrigins, operatorExternalId, pageable, accessToken);
  }

  @Override
  public DebtPositionDTO getDebtPosition(Long debtPositionId, String accessToken) {
    return client.getDebtPosition(debtPositionId, accessToken);
  }

  @Override
  public PagedModelDebtPosition getDebtPositionByDebtPositionTypeOrgId(Long debtPositionTypeOrgId,
    Pageable pageable, String accessToken) {
    return debtPositionSearchClient.getDebtPositionByDebtPositionTypeOrgId(debtPositionTypeOrgId,pageable,accessToken);
  }

  @Override
  public boolean deleteDebtPosition(Long debtPositionId, String accessToken) {
    return client.deleteDebtPosition(debtPositionId, accessToken);
  }

  @Override
  public boolean hasOperatorGrantOnDebtPosition(Long debtPositionId, Long organizationId, String operatorExternalUserId, String accessToken) {
    return debtPositionSearchClient.validateOperator(debtPositionId, organizationId, operatorExternalUserId, accessToken) == 1L;
  }

  @Override
  public DebtPositionDTO manageDebtPositionInstallments(Long debtPositionId, ManageDebtPositionDTO manageDebtPositionDTO, String accessToken) {
    return client.manageDebtPositionInstallments(debtPositionId,manageDebtPositionDTO,accessToken);
  }

  @Override
  public DebtPositionDTO publishDebtPosition(Long debtPositionId, String accessToken) {
    return client.publishDebtPosition(debtPositionId,accessToken);
  }
}
