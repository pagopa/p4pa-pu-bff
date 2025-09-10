package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.workflowhub.dto.generated.RecentScheduleExecutionInfoDTO;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class WorkflowScheduleMapperTest {
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private WorkflowScheduleMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new WorkflowScheduleMapper();
  }

  @Test
  void givenPopulatedScheduleInfoDTOWhenMapToScheduleLastUpdatedTimeDTOThenCorrectMapping() {
    ScheduleInfoDTO scheduleInfoDTO = podamFactory.manufacturePojo(ScheduleInfoDTO.class);

    RecentScheduleExecutionInfoDTO action1 = new RecentScheduleExecutionInfoDTO();
    action1.setStartedAt(OffsetDateTime.now().minusDays(2));

    RecentScheduleExecutionInfoDTO action2 = new RecentScheduleExecutionInfoDTO();
    action2.setStartedAt(OffsetDateTime.now().minusHours(1));

    scheduleInfoDTO.setRecentActions(List.of(action1, action2));

    ScheduleLastUpdatedTimeDTO result = mapper.mapToScheduleLastUpdatedTimeDTO(scheduleInfoDTO);

    TestUtils.checkNotNullFields(result);
    Assertions.assertEquals(action2.getStartedAt(), result.getLastUpdatedAt());
  }

  @Test
  void givenScheduleInfoDTOWithoutRecentActionsWhenMapToScheduleLastUpdatedTimeDTOThenThrowIllegalStateException() {
    ScheduleInfoDTO scheduleInfoDTO = podamFactory.manufacturePojo(ScheduleInfoDTO.class);
    scheduleInfoDTO.setRecentActions(Collections.emptyList());

    Assertions.assertThrows(IllegalStateException.class,
      () -> mapper.mapToScheduleLastUpdatedTimeDTO(scheduleInfoDTO), "Expected IllegalStateException when no recent actions are present");
  }

  @Test
  void givenNullScheduleInfoDTOWhenMapToScheduleLastUpdatedTimeDTOThenReturnNull() {
    ScheduleLastUpdatedTimeDTO result = mapper.mapToScheduleLastUpdatedTimeDTO(null);

    Assertions.assertNull(result);
  }
}
