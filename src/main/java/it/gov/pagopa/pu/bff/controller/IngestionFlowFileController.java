package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.IngestionFlowFilesApi;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.ingestion_flow_file.IngestionFlowFileService;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.FlowFileTypeEnum;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class IngestionFlowFileController implements IngestionFlowFilesApi {

  private final IngestionFlowFileService ingestionFlowFileService;

  public IngestionFlowFileController(
    IngestionFlowFileService ingestionFlowFileService) {
    this.ingestionFlowFileService = ingestionFlowFileService;
  }

  @Override
  public ResponseEntity<PagedIngestionFlowFile> getIngestionFlowFiles(
    Long organizationId,
    FlowFileTypeEnum flowFileType, OffsetDateTime creationDateFrom,
    OffsetDateTime creationDateTo, String status, String fileName,
    Pageable pageable) {
    log.info("User requested getIngestionFlowFiles having organizationId {} and flowFileType {}", organizationId, flowFileType);
    return ResponseEntity.ok(ingestionFlowFileService.getIngestionFlowFiles(new IngestionFlowFileFiltersDTO(organizationId,flowFileType,creationDateFrom,creationDateTo,status,fileName),pageable, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }
}
