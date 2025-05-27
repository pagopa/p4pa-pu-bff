package it.gov.pagopa.pu.bff.connector.workflow_hub.client;

import it.gov.pagopa.pu.bff.connector.workflow_hub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.springframework.stereotype.Service;

@Service
public class WorkflowHubClient {

  private final WorkflowHubApisHolder workflowHubApisHolder;

  public WorkflowHubClient (WorkflowHubApisHolder workflowHubApisHolder) {
    this.workflowHubApisHolder = workflowHubApisHolder;
  }

  public WorkflowCreatedDTO synchronizeTaxonomy(String accessToken) {
    return workflowHubApisHolder.getTaxonomyApi(accessToken)
      .synchronizeTaxonomy();
  }
}
