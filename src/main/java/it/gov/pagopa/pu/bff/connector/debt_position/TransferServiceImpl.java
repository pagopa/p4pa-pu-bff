package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.TransferSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {

  private final TransferSearchClient client;

  public TransferServiceImpl(TransferSearchClient client) {
    this.client = client;
  }

  @Override
  public CollectionModelTransfer getTransfers(Long installmentId, String operatorExternalUserId, String accessToken) {
    return client.getTransfers(installmentId, operatorExternalUserId, accessToken);
  }

}
