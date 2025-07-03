package it.gov.pagopa.pu.bff.service.schedule;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;

public interface ScheduleRetrieverService {
  ScheduleLastUpdatedTimeDTO getScheduleLastUpdatedTime(ScheduleEnum scheduleId, String accessToken);
}
