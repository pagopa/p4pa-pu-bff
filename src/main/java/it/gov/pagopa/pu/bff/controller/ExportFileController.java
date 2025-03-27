package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ExportFilesApi;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.export_flow_file.ExportFileRetrieverService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;


@Slf4j
@RestController
public class ExportFileController implements ExportFilesApi {

  private final ExportFileRetrieverService exportFileRetrieverService;

  public ExportFileController(
    ExportFileRetrieverService exportFileRetrieverService) {
    this.exportFileRetrieverService = exportFileRetrieverService;
  }

  @Override
  public ResponseEntity<PagedExportFile> getExportFiles(
    Long organizationId,
    ExportFileTypeEnum flowFileType, OffsetDateTime creationDateFrom,
    OffsetDateTime creationDateTo, ExportFileStatus status, String fileName,
    Pageable pageable) {
    log.info(
      "User requested getExportFiles having organizationId {} and flowFileType {}",
      organizationId, flowFileType);
    return ResponseEntity.ok(exportFileRetrieverService.getExportFiles(
      new ExportFileFiltersDTO(organizationId, flowFileType,
        new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo),
        status, fileName), pageable, SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> createExportFile(
    ExportFileRequestDTO requestDTO) {
    log.info(
      "User requested export file having organizationId {} and flowFileType {}",
      requestDTO.getOrganizationId(), requestDTO.getFlowFileType());

    exportFileRetrieverService.createExportFile(requestDTO, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());

    return ResponseEntity.ok().build();
  }
}
