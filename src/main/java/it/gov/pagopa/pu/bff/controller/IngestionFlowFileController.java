package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.IngestionFlowFilesApi;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.ingestion_flow_file.IngestionFlowFileRetrieverService;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.IngestionFlowFileTypeEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFileStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;


@Slf4j
@RestController
public class IngestionFlowFileController implements IngestionFlowFilesApi {

  private final IngestionFlowFileRetrieverService ingestionFlowFileRetrieverService;

  public IngestionFlowFileController(
    IngestionFlowFileRetrieverService ingestionFlowFileRetrieverService) {
    this.ingestionFlowFileRetrieverService = ingestionFlowFileRetrieverService;
  }

  @Override
  public ResponseEntity<PagedIngestionFlowFile> getIngestionFlowFiles(
    Long organizationId,
    List<IngestionFlowFileTypeEnum> ingestionFlowFileTypes, OffsetDateTime creationDateTimeFrom,
    OffsetDateTime creationDateTimeTo, IngestionFlowFileStatus status, String fileName,
    Pageable pageable) {
    log.info("User requested getIngestionFlowFiles having organizationId {} and ingestionFlowFileTypes {}", organizationId, ingestionFlowFileTypes);
    return ResponseEntity.ok(ingestionFlowFileRetrieverService.getIngestionFlowFiles(new IngestionFlowFileFiltersDTO(organizationId,ingestionFlowFileTypes,creationDateTimeFrom,creationDateTimeTo,status,fileName),pageable, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }
}
