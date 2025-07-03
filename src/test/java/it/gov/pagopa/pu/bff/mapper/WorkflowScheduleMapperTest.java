package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.OffsetDateTime;

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
    scheduleInfoDTO.setLastUpdatedAt(OffsetDateTime.now().minusDays(1));

    ScheduleLastUpdatedTimeDTO result = mapper.mapToScheduleLastUpdatedTimeDTO(scheduleInfoDTO);

    TestUtils.checkNotNullFields(result);
    Assertions.assertEquals(scheduleInfoDTO.getLastUpdatedAt(), result.getLastUpdatedAt());
  }

  @Test
  void givenNullScheduleInfoDTOWhenMapToScheduleLastUpdatedTimeDTOThenReturnNull() {
    ScheduleLastUpdatedTimeDTO result = mapper.mapToScheduleLastUpdatedTimeDTO(null);

    Assertions.assertNull(result);
  }
}
