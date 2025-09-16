package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;

@Service
@Slf4j
public class DebtPositionTypeOrgOperatorsApiClient {
  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgOperatorsApiClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public int deleteOperators(Long debtPositionTypeOrgId, Set<String> externalOperatorUserIds, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeOrgOperatorsApi(accessToken)
        .deleteOperators(debtPositionTypeOrgId, externalOperatorUserIds);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("Operator with ID %d not found".formatted(debtPositionTypeOrgId));
    }
  }
}
