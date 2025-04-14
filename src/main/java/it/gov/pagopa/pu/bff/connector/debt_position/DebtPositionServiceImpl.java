package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionClient;
import it.gov.pagopa.pu.bff.dto.DebtPositionViewFiltersDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionView;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtPositionServiceImpl implements DebtPositionService {

  private final DebtPositionClient client;

  public DebtPositionServiceImpl(DebtPositionClient client) {
    this.client = client;
  }

  @Override
  public DebtPositionDTO createDebtPosition(DebtPositionDTO debtPositionDTO, Boolean massive, String accessToken) {
    return client.createDebtPosition(debtPositionDTO, massive, accessToken);
  }

  @Override
  public PagedModelDebtPositionView getDebtPositionViews(
    DebtPositionViewFiltersDTO filtersDTO, List<String> debtPositionOrigins, String operatorExternalId,
    Pageable pageable, String accessToken) {
    return client.getDebtPositionViews(filtersDTO, debtPositionOrigins, operatorExternalId, pageable, accessToken);
  }

  @Override
  public DebtPositionDTO getDebtPosition(Long debtPositionId, String accessToken) {
    return client.getDebtPosition(debtPositionId, accessToken);
  }
}
