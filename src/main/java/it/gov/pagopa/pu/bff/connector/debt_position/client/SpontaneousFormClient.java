package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.debtpositions.dto.generated.SpontaneousForm;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    }catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("SpontaneousForm having id "+spontaneousFormId+" not found");
    }
  }

  public void updateSpontaneousForm(SpontaneousForm spontaneousForm, String accessToken) {
    try{
      debtPositionApisHolder.getSpontaneousFormApi(accessToken)
        .updateSpontaneousForm(spontaneousForm);
    }catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("SpontaneousForm having id "+spontaneousForm.getSpontaneousFormId()+" not found");
    }
  }
}
