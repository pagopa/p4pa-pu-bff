package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileSearchClient;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.mapper.ExportFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExportFileServiceImpl implements ExportFileService {
  private final ExportFileSearchClient exportFileSearchClient;
  private final ExportFileMapper exportFileMapper;

  public ExportFileServiceImpl(
    ExportFileSearchClient exportFileSearchClient,
    ExportFileMapper exportFileMapper) {
    this.exportFileSearchClient = exportFileSearchClient;
    this.exportFileMapper = exportFileMapper;
  }

  @Override
  public PagedExportFile getExportFiles(
    ExportFileFiltersDTO exportFileFiltersDTO, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    String operatorExternalUserId = null;
    if(!AuthorizationService.isAdminRole(
        exportFileFiltersDTO.getOrganizationId(), loggedUser)){
      operatorExternalUserId = loggedUser.getMappedExternalUserId();
    }

    return exportFileMapper.mapToPagedExportFile(
      exportFileSearchClient.getExportFiles(
          exportFileFiltersDTO, operatorExternalUserId, pageable, accessToken),
      loggedUser,accessToken);
  }
}
