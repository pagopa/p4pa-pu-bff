package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkflowScheduleMapper {

  public ScheduleLastUpdatedTimeDTO mapToScheduleLastUpdatedTimeDTO(ScheduleInfoDTO scheduleInfoDTO) {
    if (scheduleInfoDTO == null) {
      return null;
    }

    return ScheduleLastUpdatedTimeDTO.builder()
      .lastUpdatedAt(scheduleInfoDTO.getLastExecution())
      .build();
  }
}
