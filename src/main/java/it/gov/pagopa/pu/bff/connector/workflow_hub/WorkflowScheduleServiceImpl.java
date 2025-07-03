package it.gov.pagopa.pu.bff.connector.workflow_hub;

import it.gov.pagopa.pu.bff.connector.workflow_hub.client.WorkflowScheduleClient;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class WorkflowScheduleServiceImpl implements WorkflowScheduleService {
  private final WorkflowScheduleClient workflowScheduleClient;

  public WorkflowScheduleServiceImpl(WorkflowScheduleClient workflowScheduleClient) {
    this.workflowScheduleClient = workflowScheduleClient;
  }

  @Override
  public ScheduleInfoDTO getScheduleLastUpdatedTime(ScheduleEnum scheduleId, String accessToken) {
    return workflowScheduleClient.getScheduleLastUpdatedTime(scheduleId, accessToken);
  }
}
