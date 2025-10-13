package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.springframework.stereotype.Service;

@Service
public class SpontaneousFormClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public SpontaneousFormClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public SpontaneousForm createSpontaneousForm(SpontaneousForm spontaneousForm, String accessToken) {
    return debtPositionApisHolder.getSpontaneousFormApi(accessToken)
        .createSpontaneousForm(spontaneousForm);
  }
}
