package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.common.NotFoundException;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
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

  public void deleteSpontaneousForm(Long spontaneousFormId, String accessToken) {
   try{
     debtPositionApisHolder.getSpontaneousFormApi(accessToken)
          .deleteSpontaneousForm(spontaneousFormId);
    }catch (RestInvokeNotFoundException e) {
      throw new NotFoundException("SPONTANEOUS_FORM_NOT_FOUND", "SpontaneousForm having id "+spontaneousFormId+" not found");
    }
  }

  public void updateSpontaneousForm(SpontaneousForm spontaneousForm, String accessToken) {
    try{
      debtPositionApisHolder.getSpontaneousFormApi(accessToken)
        .updateSpontaneousForm(spontaneousForm);
    }catch (RestInvokeNotFoundException e) {
      throw new NotFoundException("SPONTANEOUS_FORM_NOT_FOUND", "SpontaneousForm having id "+spontaneousForm.getSpontaneousFormId()+" not found");
    }
  }
}
