package it.gov.pagopa.pu.bff.service.schedule;

import it.gov.pagopa.pu.bff.connector.workflow_hub.WorkflowScheduleService;
import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.bff.mapper.WorkflowScheduleMapper;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import org.springframework.stereotype.Service;

@Service
public class ScheduleRetrieverServiceImpl implements ScheduleRetrieverService {
  private final WorkflowScheduleService workflowScheduleService;
  private final WorkflowScheduleMapper workflowScheduleMapper;

  public ScheduleRetrieverServiceImpl(WorkflowScheduleService workflowScheduleService, WorkflowScheduleMapper workflowScheduleMapper) {
    this.workflowScheduleService = workflowScheduleService;
    this.workflowScheduleMapper = workflowScheduleMapper;
  }

  @Override
  public ScheduleLastUpdatedTimeDTO getScheduleLastUpdatedTime(ScheduleEnum scheduleId, String accessToken) {
    return workflowScheduleMapper.mapToScheduleLastUpdatedTimeDTO(
      workflowScheduleService.getScheduleLastUpdatedTime(scheduleId, accessToken));
  }
}
