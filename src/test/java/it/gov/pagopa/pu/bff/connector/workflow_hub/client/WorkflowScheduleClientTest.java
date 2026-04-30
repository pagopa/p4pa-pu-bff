package it.gov.pagopa.pu.bff.connector.workflow_hub.client;

import it.gov.pagopa.pu.bff.connector.workflow_hub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.workflowhub.controller.generated.ScheduleApi;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleInfoDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowScheduleClientTest {
  @Mock
  private WorkflowHubApisHolder workflowHubApisHolderMock;

  @Mock
  private ScheduleApi scheduleApiMock;

  private WorkflowScheduleClient workflowScheduleClient;

  @BeforeEach
  void setUp() {
    workflowScheduleClient = new WorkflowScheduleClient(workflowHubApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      workflowHubApisHolderMock,
      scheduleApiMock
    );
  }

  @Test
  void whenGetScheduleLastUpdatedTimeThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    ScheduleInfoDTO expected = new ScheduleInfoDTO();

    Mockito.when(workflowHubApisHolderMock.getScheduleApi(accessToken))
      .thenReturn(scheduleApiMock);
    Mockito.when(scheduleApiMock.getScheduleInfo(ScheduleEnum.SYNCHRONIZE_TAXONOMY_PAGOPA_FETCH))
      .thenReturn(expected);

    ScheduleInfoDTO result = workflowScheduleClient.getScheduleLastUpdatedTime(ScheduleEnum.SYNCHRONIZE_TAXONOMY_PAGOPA_FETCH, accessToken);

    Assertions.assertSame(expected, result);
  }
}
