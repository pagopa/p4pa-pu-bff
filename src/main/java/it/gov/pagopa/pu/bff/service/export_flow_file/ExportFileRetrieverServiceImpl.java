package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.ExportFileService;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequestDTO;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import it.gov.pagopa.pu.bff.mapper.ExportFileMapper;
import it.gov.pagopa.pu.bff.mapper.ExportFileRequestDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExportFileRetrieverServiceImpl implements
  ExportFileRetrieverService {

  private final ExportFileService exportFileService;
  private final ExportFileMapper exportFileMapper;
  private final ExportFileRequestDTOMapper exportFileRequestDTOMapper;

  public ExportFileRetrieverServiceImpl(
    ExportFileService exportFileService,
    ExportFileMapper exportFileMapper,
    ExportFileRequestDTOMapper exportFileRequestDTOMapper) {
    this.exportFileService = exportFileService;
    this.exportFileMapper = exportFileMapper;
    this.exportFileRequestDTOMapper = exportFileRequestDTOMapper;
  }

  @Override
  public PagedExportFile getExportFiles(
    ExportFileFiltersDTO exportFileFiltersDTO, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    String operatorExternalUserId = null;
    if (!AuthorizationService.isAdminRole(
      exportFileFiltersDTO.getOrganizationId(), loggedUser)) {
      operatorExternalUserId = loggedUser.getMappedExternalUserId();
    }

    return exportFileMapper.mapToPagedExportFile(
      exportFileService.getExportFiles(
        exportFileFiltersDTO, operatorExternalUserId, pageable, accessToken),
      loggedUser, accessToken);
  }

  @Override
  public void createPaidExportFile(PaidExportFileRequestDTO requestDTO,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(requestDTO.getOrganizationId(), loggedUser);
    exportFileService.createPaidExportFile(
      exportFileRequestDTOMapper.map2ProcessExecutionsDto(requestDTO),
      accessToken);
  }

  @Override
  public void createClassificationsExportFile(ClassificationsExportFileRequestDTO requestDTO,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(requestDTO.getOrganizationId(), loggedUser);
    exportFileService.createClassificationsExportFile(requestDTO, accessToken);
  }

  @Override
  public void createPaymentsReportingExportFile(PaymentsReportingExportFileRequestDTO requestDTO,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(requestDTO.getOrganizationId(), loggedUser);
    exportFileService.createPaymentsReportingExportFile(requestDTO, accessToken);
  }

  @Override
  public void createReceiptsArchivingExportFile(
    ReceiptsArchivingExportFileRequestDTO receiptsArchivingExportFileRequestDTO,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(
      receiptsArchivingExportFileRequestDTO.getOrganizationId(), loggedUser);
    exportFileService.createReceiptsArchivingExportFile(
      exportFileRequestDTOMapper.map2ProcessExecutionsDto(receiptsArchivingExportFileRequestDTO),
      accessToken);
  }
}
