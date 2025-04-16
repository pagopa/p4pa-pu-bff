package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.DebtPositionTypeClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
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
  public PagedModelDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long brokerId, String description, Pageable pageable, String accessToken) {
    return client.getDebtPositionTypeWithCount(brokerId, description, pageable, accessToken);
  }

  @Override
  public DebtPositionType createDebtPositionType(
    DebtPositionTypeRequestBody debtPositionType,
    String accessToken) {
    return client.createDebtPositionType(debtPositionType, accessToken);
  }

  @Override
  public DebtPositionType patchDebtPositionType(
    Long debtPositionTypeId,
    DebtPositionTypeRequestBody debtPositionType,
    String accessToken) {
    return client.patchDebtPositionType(debtPositionTypeId, debtPositionType, accessToken);
  }

  @Override
  public void deleteDebtPositionType(Long debtPositionTypeId, String accessToken) {
    client.deleteDebtPositionType(debtPositionTypeId, accessToken);
  }
}
