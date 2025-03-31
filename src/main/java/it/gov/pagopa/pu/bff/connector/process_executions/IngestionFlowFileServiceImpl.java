package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.connector.process_executions.client.IngestionFlowFileSearchClient;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IngestionFlowFileServiceImpl implements IngestionFlowFileService {

  private final IngestionFlowFileSearchClient client;

  public IngestionFlowFileServiceImpl(IngestionFlowFileSearchClient client) {
    this.client = client;
  }

  @Override
  public PagedModelIngestionFlowFile getIngestionFlowFiles(IngestionFlowFileFiltersDTO ingestionFlowFileFilters, String operatorExternalId, Pageable pageable, String accessToken) {
    return client.getIngestionFlowFiles(ingestionFlowFileFilters, operatorExternalId, pageable, accessToken);
  }
}
