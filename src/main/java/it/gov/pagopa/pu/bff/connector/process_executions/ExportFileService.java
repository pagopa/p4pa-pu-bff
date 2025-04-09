package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import org.springframework.data.domain.Pageable;

public interface ExportFileService {

  PagedModelExportFile getExportFiles(ExportFileFiltersDTO exportFileFilters,
    String operatorExternalId, Pageable pageable, String accessToken);

  void createPaidExportFile(PaidExportFileRequestDTO requestDTO, String accessToken);
  void createClassificationsExportFile(ClassificationsExportFileRequestDTO requestDTO, String accessToken);
  void createPaymentsReportingExportFile(PaymentsReportingExportFileRequestDTO requestDTO, String accessToken);
}
