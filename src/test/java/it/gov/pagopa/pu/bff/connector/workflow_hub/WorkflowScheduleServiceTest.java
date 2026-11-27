package it.gov.pagopa.pu.bff.connector.workflow_hub;

import it.gov.pagopa.pu.bff.connector.workflow_hub.client.WorkflowScheduleClient;
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
class WorkflowScheduleServiceTest {

  @Mock
  private WorkflowScheduleClient workflowScheduleClientMock;

  @InjectMocks
  private WorkflowScheduleServiceImpl workflowScheduleService;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(workflowScheduleClientMock);
  }

  @Test
  void givenValidScheduleIdAndAccessTokenWhenGetScheduleLastUpdatedTimeThenReturnDTO() {
    String accessToken = "fakeAccessToken";
    ScheduleEnum scheduleId = ScheduleEnum.SYNCHRONIZE_TAXONOMY_PAGOPA_FETCH;

    ScheduleInfoDTO scheduleInfoDTO = podamFactory.manufacturePojo(ScheduleInfoDTO.class);
    scheduleInfoDTO.setScheduleId(scheduleId);

    when(workflowScheduleClientMock.getScheduleLastUpdatedTime(scheduleId, accessToken))
      .thenReturn(scheduleInfoDTO);


    ScheduleInfoDTO result = workflowScheduleService.getScheduleLastUpdatedTime(scheduleId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(scheduleInfoDTO, result);
  }
}

