package it.gov.pagopa.pu.bff.service;

import it.gov.pagopa.pu.bff.connector.workflow_hub.WorkflowScheduleService;
import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.bff.mapper.WorkflowScheduleMapper;
import it.gov.pagopa.pu.bff.service.schedule.ScheduleRetrieverServiceImpl;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleRetrieverServiceImplTest {

  @Mock
  private WorkflowScheduleService workflowScheduleServiceMock;

  @Mock
  private WorkflowScheduleMapper workflowScheduleMapperMock;

  @InjectMocks
  private ScheduleRetrieverServiceImpl scheduleRetrieverService;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(workflowScheduleServiceMock, workflowScheduleMapperMock);
  }

  @Test
  void givenValidScheduleIdAndAccessTokenWhenGetScheduleLastUpdatedTimeThenReturnMappedDTO() {
    String accessToken = "fakeAccessToken";
    ScheduleEnum scheduleId = ScheduleEnum.SYNCHRONIZE_TAXONOMY_PAGOPA_FETCH;

    ScheduleInfoDTO scheduleInfoDTO = podamFactory.manufacturePojo(ScheduleInfoDTO.class);
    scheduleInfoDTO.setScheduleId(scheduleId);

    ScheduleLastUpdatedTimeDTO expectedDto = ScheduleLastUpdatedTimeDTO.builder()
      .lastUpdatedAt(scheduleInfoDTO.getLastUpdatedAt())
      .build();

    when(workflowScheduleServiceMock.getScheduleLastUpdatedTime(scheduleId, accessToken))
      .thenReturn(scheduleInfoDTO);

    when(workflowScheduleMapperMock.mapToScheduleLastUpdatedTimeDTO(scheduleInfoDTO))
      .thenReturn(expectedDto);

    ScheduleLastUpdatedTimeDTO result = scheduleRetrieverService.getScheduleLastUpdatedTime(scheduleId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedDto, result);
  }
}

