package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class IngestionFlowFileSearchClient {

    private final ProcessExecutionsApisHolder processExecutionsApisHolder;

    public IngestionFlowFileSearchClient(
      ProcessExecutionsApisHolder processExecutionsApisHolder) {
        this.processExecutionsApisHolder = processExecutionsApisHolder;
    }

  public PagedModelIngestionFlowFile getIngestionFlowFiles(IngestionFlowFileFiltersDTO ingestionFlowFileFilters, String operatorExternalId, Pageable pageable, String accessToken) {
    try {
      Integer pageNumber = PageUtils.getPageNumber(pageable);
      Integer pageSize = PageUtils.getPageSize(pageable);
      return processExecutionsApisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
        .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
          String.valueOf(ingestionFlowFileFilters.getOrganizationId()),
          ingestionFlowFileFilters.getFlowFileType().toString(),
          ingestionFlowFileFilters.getCreationDateFrom(),
          ingestionFlowFileFilters.getCreationDateTo(),
          ingestionFlowFileFilters.getStatus(),
          ingestionFlowFileFilters.getFileName(),
          operatorExternalId,
          pageNumber,
          pageSize,
          PageUtils.getSortList(pageable));
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving ingestion flow files", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving ingestion flow files", e);
      throw e;
    }
  }


}
