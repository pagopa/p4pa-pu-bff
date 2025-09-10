package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.schedule.ScheduleRetrieverService;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {
  @Mock
  private ScheduleRetrieverService scheduleRetrieverServiceMock;

  @InjectMocks
  private ScheduleController scheduleController;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(scheduleRetrieverServiceMock);
  }

  @Test
  void givenCorrectRequestWhenGetScheduleLastUpdatedTimeThenOk() {
    String accessToken = "fakeAccessToken";
    ScheduleEnum scheduleId = ScheduleEnum.SYNCHRONIZE_TAXONOMY_PAGOPA_FETCH;
    ScheduleLastUpdatedTimeDTO expectedDto = new ScheduleLastUpdatedTimeDTO();
    expectedDto.setLastStartedAt(OffsetDateTime.now().minusDays(1));

    try (MockedStatic<SecurityUtils> mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
      mockedSecurityUtils.when(SecurityUtils::getAccessToken).thenReturn(accessToken);

      Mockito.when(scheduleRetrieverServiceMock.getScheduleLastUpdatedTime(scheduleId, accessToken))
        .thenReturn(expectedDto);

      ResponseEntity<ScheduleLastUpdatedTimeDTO> response = scheduleController.getScheduleLastUpdatedTime(scheduleId);

      Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
      Assertions.assertNotNull(response.getBody());
      Assertions.assertSame(expectedDto, response.getBody());
    }
  }
}
