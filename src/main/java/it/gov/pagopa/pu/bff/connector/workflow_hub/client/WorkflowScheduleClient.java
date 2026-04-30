package it.gov.pagopa.pu.bff.connector.workflow_hub.client;

import it.gov.pagopa.pu.bff.connector.workflow_hub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class WorkflowScheduleClient {
  private final WorkflowHubApisHolder workflowHubApisHolder;

  public WorkflowScheduleClient(WorkflowHubApisHolder workflowHubApisHolder) {
    this.workflowHubApisHolder = workflowHubApisHolder;
  }

  public ScheduleInfoDTO getScheduleLastUpdatedTime(ScheduleEnum scheduleId, String accessToken) {
    return workflowHubApisHolder.getScheduleApi(accessToken)
      .getScheduleInfo(scheduleId);
  }
}
