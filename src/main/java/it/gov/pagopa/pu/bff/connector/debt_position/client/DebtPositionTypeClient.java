package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.DebtPositionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class DebtPositionTypeClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public DebtPositionType getDebtPositionTypeById(Long id, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudGetDebtpositiontype(String.valueOf(id));
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("Debt Position Type with ID {} not found", id);
        return null;
      }
      log.error("Error retrieving Debt Position Type with ID: {}", id, e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving Debt Position Type with ID: {}", id, e);
      throw e;
    }
  }

}

