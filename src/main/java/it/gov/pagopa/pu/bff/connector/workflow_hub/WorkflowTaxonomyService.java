package it.gov.pagopa.pu.bff.connector.workflow_hub;

import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;

public interface WorkflowTaxonomyService {
  WorkflowCreatedDTO synchronizeTaxonomy(String accessToken);
}
