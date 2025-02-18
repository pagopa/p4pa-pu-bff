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

@Service
@Slf4j
public class IngestionFlowFileSearchClient {

  private final ProcessExecutionsApisHolder processExecutionsApisHolder;

  public IngestionFlowFileSearchClient(
    ProcessExecutionsApisHolder processExecutionsApisHolder) {
    this.processExecutionsApisHolder = processExecutionsApisHolder;
  }

  public PagedModelIngestionFlowFile getIngestionFlowFiles(IngestionFlowFileFiltersDTO ingestionFlowFileFilters, String operatorExternalId, Pageable pageable, String accessToken) {
    return processExecutionsApisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
      .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
        String.valueOf(ingestionFlowFileFilters.getOrganizationId()),
        ingestionFlowFileFilters.getFlowFileType().stream().map(
          FlowFileTypeEnum::toString).toList(),
        DateUtils.toLocalDateTime(ingestionFlowFileFilters.getCreationDateFrom()),
        DateUtils.toLocalDateTime(ingestionFlowFileFilters.getCreationDateTo()),
        ingestionFlowFileFilters.getStatus().name(),
        ingestionFlowFileFilters.getFileName(),
        operatorExternalId,
        PageUtils.getPageNumber(pageable),
        PageUtils.getPageSize(pageable),
        PageUtils.getSortList(pageable));
  }


}
