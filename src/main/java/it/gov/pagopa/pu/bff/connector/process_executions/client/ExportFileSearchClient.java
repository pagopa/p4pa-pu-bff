package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExportFileSearchClient {

    private final ProcessExecutionsApisHolder processExecutionsApisHolder;

    public ExportFileSearchClient(
      ProcessExecutionsApisHolder processExecutionsApisHolder) {
        this.processExecutionsApisHolder = processExecutionsApisHolder;
    }

  public PagedModelExportFile getExportFiles(ExportFileFiltersDTO exportFileFilters, String operatorExternalId, Pageable pageable, String accessToken) {
    try {
      return processExecutionsApisHolder.getExportFileSearchControllerApi(accessToken)
        .crudExportFilesFindByOrganizationIDFlowTypeCreateDate(
          String.valueOf(exportFileFilters.getOrganizationId()),
          exportFileFilters.getFlowFileType().toString(),
          exportFileFilters.getCreationDateFrom(),
          exportFileFilters.getCreationDateTo(),
          exportFileFilters.getStatus(),
          exportFileFilters.getFileName(),
          operatorExternalId,
          PageUtils.getPageNumber(pageable),
          PageUtils.getPageSize(pageable),
          PageUtils.getSortList(pageable));
    } catch (Exception e) {
      log.error("Unexpected error while retrieving ingestion flow files", e);
      throw e;
    }
  }

}
