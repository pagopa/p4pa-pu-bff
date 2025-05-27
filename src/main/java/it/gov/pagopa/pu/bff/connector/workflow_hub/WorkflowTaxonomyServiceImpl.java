package it.gov.pagopa.pu.bff.connector.workflow_hub;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.springframework.stereotype.Service;

@Service
public class WorkflowTaxonomyServiceImpl implements WorkflowTaxonomyService {

  private final TaxonomyClient taxonomyClient;

  public WorkflowTaxonomyServiceImpl(TaxonomyClient taxonomyClient) {
    this.taxonomyClient = taxonomyClient;
  }

  @Override
  public WorkflowCreatedDTO synchronizeTaxonomy(String accessToken) {
    return taxonomyClient.synchronizeTaxonomy(accessToken);
  }
}
