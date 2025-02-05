package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
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
      Integer pageNumber = pageable.isPaged()? pageable.getPageNumber() : 0;
      Integer pageSize = pageable.isPaged()? pageable.getPageSize() : null;
      String organizationId = ingestionFlowFileFilters != null ? String.valueOf(
        ingestionFlowFileFilters.getOrganizationId()) : null;
      String flowFileType = ingestionFlowFileFilters != null
        ? ingestionFlowFileFilters.getFlowFileType() : null;
      OffsetDateTime creationDateFrom = ingestionFlowFileFilters != null
        ? ingestionFlowFileFilters.getCreationDateFrom() : null;
      OffsetDateTime creationDateTo = ingestionFlowFileFilters != null
        ? ingestionFlowFileFilters.getCreationDateTo() : null;
      String status =
        ingestionFlowFileFilters != null ? ingestionFlowFileFilters.getStatus()
          : null;
      String fileMame = ingestionFlowFileFilters != null
        ? ingestionFlowFileFilters.getFileName() : null;
      return processExecutionsApisHolder.getIngestionFlowFileSearchControllerApi(accessToken)
        .crudIngestionFlowFilesFindByOrganizationIDFlowTypeCreateDate(
          organizationId,flowFileType,creationDateFrom,creationDateTo,status,fileMame,
          operatorExternalId,
          pageNumber,
          pageSize,
          getSortList(pageable));
    } catch (HttpClientErrorException e) {
      log.error("Error while retrieving ingestion flow files", e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving ingestion flow files", e);
      throw e;
    }
  }

  private static List<String> getSortList(Pageable pageable) {
    return pageable.getSort().isSorted()?
      pageable.getSort().stream()
        .map(o -> o.getProperty() + "," + o.getDirection()).toList()
      : Collections.emptyList();
  }
}
