package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import org.springframework.data.domain.Pageable;

public interface IngestionFlowFileService {
  PagedModelIngestionFlowFile getIngestionFlowFiles(IngestionFlowFileFiltersDTO ingestionFlowFileFilters, String operatorExternalId, Pageable pageable, String accessToken);
}
