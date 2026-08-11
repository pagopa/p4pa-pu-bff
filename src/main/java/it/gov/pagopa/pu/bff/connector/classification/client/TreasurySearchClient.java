package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TreasurySearchClient {

  private final ClassificationApisHolder classificationApisHolder;

  public TreasurySearchClient(ClassificationApisHolder classificationApisHolder) {
    this.classificationApisHolder = classificationApisHolder;
  }

  public Treasury getTreasuryDetail(Long organizationId, String treasuryId, String accessToken) {
    try {
      return classificationApisHolder.getTreasurySearchControllerApi(accessToken)
        .crudTreasuryFindByOrganizationIdAndTreasuryId(organizationId, treasuryId);
    } catch (RestInvokeNotFoundException e) {
      log.warn("TreasuryDetail with organizationId {} and treasuryId {} not found", organizationId, treasuryId);
      return null;
    }
  }

}
