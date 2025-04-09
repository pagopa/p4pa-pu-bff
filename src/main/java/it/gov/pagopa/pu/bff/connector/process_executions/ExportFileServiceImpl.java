package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileClient;
import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileSearchClient;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExportFileServiceImpl implements ExportFileService {

  private final ExportFileClient client;
  private final ExportFileSearchClient searchClient;

  public ExportFileServiceImpl(ExportFileClient client, ExportFileSearchClient searchClient) {
    this.client = client;
    this.searchClient = searchClient;
  }

  @Override
  public PagedModelExportFile getExportFiles(
    ExportFileFiltersDTO exportFileFilters,
    String operatorExternalId, Pageable pageable, String accessToken) {
    return searchClient.getExportFiles(exportFileFilters, operatorExternalId,
      pageable, accessToken);
  }

  @Override
  public void createPaidExportFile(PaidExportFileRequestDTO requestDTO, String accessToken) {
    client.createPaidExportFile(requestDTO, accessToken);
  }

  @Override
  public void createClassificationsExportFile(
    ClassificationsExportFileRequestDTO requestDTO, String accessToken) {
    client.createClassificationsExportFile(requestDTO, accessToken);
  }

  @Override
  public void createPaymentsReportingExportFile(
    PaymentsReportingExportFileRequestDTO requestDTO, String accessToken) {
    client.createPaymentsReportingExportFile(requestDTO, accessToken);
  }
}
