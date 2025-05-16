package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequest;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequest;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import org.springframework.data.domain.Pageable;

public interface ExportFileRetrieverService {

  PagedExportFile getExportFiles(ExportFileFiltersDTO exportFileFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

  void createPaidExportFile(PaidExportFileRequest requestDTO, UserInfo user, String accessToken);
  void createClassificationsExportFile(
    ClassificationsExportFileRequestDTO requestDTO, UserInfo user, String accessToken);
  void createPaymentsReportingExportFile(
    PaymentsReportingExportFileRequestDTO requestDTO, UserInfo user, String accessToken);
  void createReceiptsArchivingExportFile(
    ReceiptsArchivingExportFileRequest receiptsArchivingExportFileRequestDTO, UserInfo user, String accessToken);
}
