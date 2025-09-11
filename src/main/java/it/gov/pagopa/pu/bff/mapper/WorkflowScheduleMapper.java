package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.workflowhub.dto.generated.RecentScheduleExecutionInfoDTO;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Comparator;

@Component
public class WorkflowScheduleMapper {

  public ScheduleLastUpdatedTimeDTO mapToScheduleLastUpdatedTimeDTO(ScheduleInfoDTO scheduleInfoDTO) {
    if (scheduleInfoDTO == null) {
      return null;
    }

    OffsetDateTime lastStartedAt = scheduleInfoDTO.getRecentActions().stream()
      .map(RecentScheduleExecutionInfoDTO::getStartedAt)
      .max(Comparator.naturalOrder())
      .orElseThrow(() -> new IllegalStateException("No actions found"));

    return ScheduleLastUpdatedTimeDTO.builder()
      .lastUpdatedAt(lastStartedAt)
      .build();
  }
}
