package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileClient;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExportFileServiceImpl implements ExportFileService {

  private final ExportFileClient client;

  public ExportFileServiceImpl(ExportFileClient client) {
    this.client = client;
  }

  @Override
  public PagedModelExportFile getExportFiles(
    ExportFileFiltersDTO exportFileFilters,
    String operatorExternalId, Pageable pageable, String accessToken) {
    return client.getExportFiles(exportFileFilters, operatorExternalId,
      pageable, accessToken);
  }

  @Override
  public void createExportFile(ExportFileRequestDTO requestDTO, String accessToken) {
    client.createExportFile(requestDTO, accessToken);
  }
}
