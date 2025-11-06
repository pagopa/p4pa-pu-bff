package it.gov.pagopa.pu.bff.service.transfer;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.debtpositions.dto.generated.Transfer;

import java.util.List;

public interface TransferRetrieverService {
  List<Transfer> getTransfers(Long organizationId, Long installmentId, UserInfo loggedUser, String accessToken);
  void validateTaxonomyCategory(String taxonomyCategory, String accessToken);
}
