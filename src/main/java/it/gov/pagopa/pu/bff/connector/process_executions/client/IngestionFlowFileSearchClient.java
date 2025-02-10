package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.FlowFileTypeEnum;
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
      return processExecutionsApisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
        .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
          String.valueOf(ingestionFlowFileFilters.getOrganizationId()),
          ingestionFlowFileFilters.getFlowFileType().stream().map(
            FlowFileTypeEnum::toString).toList(),
          DateUtils.toLocalDateTime(ingestionFlowFileFilters.getCreationDateFrom()),
          DateUtils.toLocalDateTime(ingestionFlowFileFilters.getCreationDateTo()),
          ingestionFlowFileFilters.getStatus(),
          ingestionFlowFileFilters.getFileName(),
          operatorExternalId,
          PageUtils.getPageNumber(pageable),
          PageUtils.getPageSize(pageable),
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
