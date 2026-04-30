package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import org.springframework.stereotype.Service;

@Service
public class TransferClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public TransferClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public boolean validateTaxonomyCategory(String taxonomyCategory, String orgFiscalCode, String accessToken) {
    return debtPositionApisHolder.getTransferApi(accessToken).validateTaxonomyCategory(taxonomyCategory, orgFiscalCode);
  }

}
