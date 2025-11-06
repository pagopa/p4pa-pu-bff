package it.gov.pagopa.pu.bff.connector.debt_position;

import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;

public interface TransferService {
  CollectionModelTransfer getTransfers(Long installmentId, String operatorExternalUserId, String accessToken);
  void validateTaxonomyCategory(String taxonomyCategory, String accessToken);
}
