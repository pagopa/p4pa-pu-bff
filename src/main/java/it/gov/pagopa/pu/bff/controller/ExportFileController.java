package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ExportFilesApi;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.export_flow_file.ExportFileService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.FlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class ExportFileController implements ExportFilesApi {

  private final ExportFileService exportFileService;

  public ExportFileController(
    ExportFileService exportFileService) {
    this.exportFileService = exportFileService;
  }

  @Override
  public ResponseEntity<PagedExportFile> getExportFiles(
    Long organizationId,
    FlowFileTypeEnum flowFileType, OffsetDateTime creationDateFrom,
    OffsetDateTime creationDateTo, StatusEnum status, String fileName,
    Pageable pageable) {
    log.info(
      "User requested getExportFiles having organizationId {} and flowFileType {}",
      organizationId, flowFileType);
    return ResponseEntity.ok(exportFileService.getExportFiles(
      new ExportFileFiltersDTO(organizationId, flowFileType,
        new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo),
        status, fileName), pageable, SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }
}
