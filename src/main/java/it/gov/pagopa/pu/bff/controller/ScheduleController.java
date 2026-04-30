package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ScheduleApi;
import it.gov.pagopa.pu.bff.dto.generated.ScheduleLastUpdatedTimeDTO;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.schedule.ScheduleRetrieverService;
import it.gov.pagopa.pu.workflowhub.dto.generated.ScheduleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ScheduleController implements ScheduleApi {

  private final ScheduleRetrieverService scheduleRetrieverService;

  public ScheduleController(ScheduleRetrieverService scheduleRetrieverService) {
    this.scheduleRetrieverService = scheduleRetrieverService;
  }

  @Override
  public ResponseEntity<ScheduleLastUpdatedTimeDTO> getScheduleLastUpdatedTime(ScheduleEnum scheduleId) {
    log.info("Requesting getScheduleLastUpdatedTime of {}", scheduleId);
    return ResponseEntity.ok(scheduleRetrieverService.getScheduleLastUpdatedTime(scheduleId, SecurityUtils.getAccessToken()));
  }
}
