package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import org.springframework.data.domain.Pageable;

public interface ExportFileRetrieverService {

  PagedExportFile getExportFiles(ExportFileFiltersDTO exportFileFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

  void createPaidExportFile(PaidExportFileRequestDTO requestDTO, UserInfo user, String accessToken);
  void createClassificationsExportFile(
    ClassificationsExportFileRequestDTO requestDTO, UserInfo user, String accessToken);
  void createPaymentsReportingExportFile(
    PaymentsReportingExportFileRequestDTO requestDTO, UserInfo user, String accessToken);
  void createReceiptsArchivingExportFile(
    ReceiptsArchivingExportFileRequestDTO receiptsArchivingExportFileRequestDTO, UserInfo user, String accessToken);
}
