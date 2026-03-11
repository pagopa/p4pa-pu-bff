package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import org.springframework.stereotype.Service;

@Service
public class TransferSearchClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public TransferSearchClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelTransfer getTransfers(Long installmentId, String operatorExternalUserId, String accessToken) {
    return debtPositionApisHolder.getTransferSearchControllerApi(accessToken)
      .crudTransfersFindAuthorizedByInstallmentId(installmentId, operatorExternalUserId);
  }

}
