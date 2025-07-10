package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.ExportFilesApi;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequest;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequest;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.export_flow_file.ExportFileRetrieverService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.ExportFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileStatus;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
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
    ExportFileTypeEnum exportFileType, OffsetDateTime creationDateFrom,
    OffsetDateTime creationDateTo, ExportFileStatus status, String fileName,
    Pageable pageable) {
    log.info(
      "User requested getExportFiles having organizationId {} and exportFileType {}",
      organizationId, exportFileType);
    return ResponseEntity.ok(exportFileRetrieverService.getExportFiles(
      new ExportFileFiltersDTO(organizationId, exportFileType,
        new OffsetDateTimeIntervalFilter(creationDateFrom, creationDateTo),
        status, fileName), pageable, SecurityUtils.getLoggedUser(),
      SecurityUtils.getAccessToken()));
  }

  @Override
  public ResponseEntity<Void> createPaidExportFile(
    PaidExportFileRequest requestDTO) {
    log.info(
      "User requested paid export file having organizationId {}",
      requestDTO.getOrganizationId());

    exportFileRetrieverService.createPaidExportFile(requestDTO, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> createClassificationsExportFile(
    ClassificationsExportFileRequestDTO requestDTO) {
    log.info(
      "User requested classifications export file having organizationId {}",
      requestDTO.getOrganizationId());

    exportFileRetrieverService.createClassificationsExportFile(requestDTO, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> createPaymentsReportingExportFile(
    PaymentsReportingExportFileRequestDTO requestDTO) {
    log.info(
      "User requested payments reporting export file having organizationId {}",
      requestDTO.getOrganizationId());

    exportFileRetrieverService.createPaymentsReportingExportFile(requestDTO, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());

    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> createReceiptsArchivingExportFile(
    ReceiptsArchivingExportFileRequest receiptsArchivingExportFileRequestDTO) {
    log.info(
      "User requested receipts archiving export file having organizationId {}",
      receiptsArchivingExportFileRequestDTO.getOrganizationId());

    exportFileRetrieverService.createReceiptsArchivingExportFile(receiptsArchivingExportFileRequestDTO, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken());

    return ResponseEntity.ok().build();
  }
}
