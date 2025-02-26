package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import org.springframework.data.domain.Pageable;

public interface ExportFileRetrieverService {

  PagedExportFile getExportFiles(ExportFileFiltersDTO exportFileFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken);

  void createExportFile(ExportFileRequestDTO requestDTO, String accessToken);
}
