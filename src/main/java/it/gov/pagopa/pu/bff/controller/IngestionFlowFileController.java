package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.IngestionFlowFileControllerApi;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.ingestion_flow_file.IngestionFlowFileService;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class IngestionFlowFileController implements IngestionFlowFileControllerApi {

  private final IngestionFlowFileService ingestionFlowFileService;

  public IngestionFlowFileController(
    IngestionFlowFileService ingestionFlowFileService) {
    this.ingestionFlowFileService = ingestionFlowFileService;
  }

  @Override
  public ResponseEntity<PagedIngestionFlowFile> getIngestionFlowFiles(
    Long organizationId,
    String flowFileType, OffsetDateTime creationDateFrom,
    OffsetDateTime creationDateTo, String status, String fileName,
    Pageable pageable) {
    return ResponseEntity.ok(ingestionFlowFileService.getIngestionFlowFiles(new IngestionFlowFileFiltersDTO(organizationId,flowFileType,creationDateFrom,creationDateTo,status,fileName),pageable, SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()));
  }
}
