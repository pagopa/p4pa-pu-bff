package it.gov.pagopa.pu.bff.service.transfer;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.debt_position.TransferService;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import it.gov.pagopa.pu.debtpositions.dto.generated.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TransferRetrieverServiceImpl implements TransferRetrieverService {

  private final TransferService transferService;

  public TransferRetrieverServiceImpl(TransferService transferService) {
    this.transferService = transferService;
  }

  @Override
  public List<TransferResponse> getTransfers(Long organizationId, Long installmentId, UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(organizationId, loggedUser);
    CollectionModelTransfer collection = transferService.getTransfers(installmentId, loggedUser.getMappedExternalUserId(), accessToken);

    if (collection == null || collection.getEmbedded() ==  null || collection.getEmbedded().getTransfers() == null){
      return Collections.emptyList();
    }
    return collection.getEmbedded().getTransfers();
  }

}
