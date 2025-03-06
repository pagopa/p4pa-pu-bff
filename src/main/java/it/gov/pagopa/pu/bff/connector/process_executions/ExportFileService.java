package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import org.springframework.data.domain.Pageable;

public interface ExportFileService {

  PagedModelExportFile getExportFiles(ExportFileFiltersDTO exportFileFilters,
    String operatorExternalId, Pageable pageable, String accessToken);

  void createExportFile(ExportFileRequestDTO requestDTO, String accessToken);
}
