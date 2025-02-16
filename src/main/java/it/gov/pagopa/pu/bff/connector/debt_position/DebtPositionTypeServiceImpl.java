package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtPositionTypeServiceImpl implements DebtPositionTypeService {

  private final DebtPositionTypeClient client;

  public DebtPositionTypeServiceImpl(DebtPositionTypeClient client) {
    this.client = client;
  }

  @Override
  public DebtPositionType getDebtPositionTypeById(Long id, String accessToken) {
    return client.getDebtPositionTypeById(id, accessToken);
  }

  @Override
  public PagedModelDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long brokerId, Pageable pageable, String accessToken) {
    return client.getDebtPositionTypeWithCount(brokerId, pageable, accessToken);
  }
}
