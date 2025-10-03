package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelSpontaneousForm;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SpontaneousFormSearchClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public SpontaneousFormSearchClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public List<SpontaneousForm> findAllByOrganizationId(Long organizationId, String accessToken) {
    CollectionModelSpontaneousForm collectionModelSpontaneousForm = debtPositionApisHolder.getSpontaneousFormSearchControllerApi(accessToken)
        .crudSpontaneousFormsFindAllByOrganizationId(organizationId);
    return collectionModelSpontaneousForm != null && collectionModelSpontaneousForm.getEmbedded()!=null?collectionModelSpontaneousForm.getEmbedded().getSpontaneousForms():Collections.emptyList();
  }

}
