package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

@ExtendWith(MockitoExtension.class)
class WorkflowScheduleMapperTest {
  private WorkflowScheduleMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new WorkflowScheduleMapper();
  }

  @Test
  void givenScheduleInfoDTOWithLastExecutionWhenMapThenCorrectMapping() {
    OffsetDateTime lastExecution = OffsetDateTime.now();

    ScheduleInfoDTO scheduleInfoDTO = new ScheduleInfoDTO();
    scheduleInfoDTO.setLastExecution(lastExecution);

    ScheduleLastUpdatedTimeDTO result = mapper.mapToScheduleLastUpdatedTimeDTO(scheduleInfoDTO);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(lastExecution, result.getLastUpdatedAt());
  }

  @Test
  void givenNullScheduleInfoDTOWhenMapThenReturnNull() {
    ScheduleLastUpdatedTimeDTO result = mapper.mapToScheduleLastUpdatedTimeDTO(null);

    Assertions.assertNull(result);
  }
}
