package it.gov.pagopa.pu.bff.connector.workflow_hub;

import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;

public interface WorkflowScheduleService {
  ScheduleInfoDTO getScheduleLastUpdatedTime(ScheduleEnum scheduleId, String accessToken);
}
