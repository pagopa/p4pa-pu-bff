package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.bff.connector.debt_position.client.TransferClient;
import it.gov.pagopa.pu.bff.connector.debt_position.client.TransferSearchClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService {

  private final TransferClient client;
  private final TransferSearchClient searchClient;

  public TransferServiceImpl(TransferClient client, TransferSearchClient searchClient) {
    this.client = client;
    this.searchClient = searchClient;
  }

  @Override
  public CollectionModelTransfer getTransfers(Long installmentId, String operatorExternalUserId, String accessToken) {
    return searchClient.getTransfers(installmentId, operatorExternalUserId, accessToken);
  }

  @Override
  public void validateTaxonomyCategory(String taxonomyCategory, String accessToken) {
    client.validateTaxonomyCategory(taxonomyCategory, accessToken);
  }

}
