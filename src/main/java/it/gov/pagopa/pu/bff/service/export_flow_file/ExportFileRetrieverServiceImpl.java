package it.gov.pagopa.pu.bff.service.export_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.ExportFileService;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequest;
import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequest;
import it.gov.pagopa.pu.bff.mapper.ExportFileMapper;
import it.gov.pagopa.pu.bff.mapper.export_file.PaidExportFileRequestDTOMapper;
import it.gov.pagopa.pu.bff.mapper.export_file.ReceiptsArchivingExportFileRequestDTOMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.processexecutions.dto.generated.ClassificationsExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaymentsReportingExportFileRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExportFileRetrieverServiceImpl implements
  ExportFileRetrieverService {

  private final ExportFileService exportFileService;
  private final ExportFileMapper exportFileMapper;
  private final PaidExportFileRequestDTOMapper paidExportFileRequestDTOMapper;
  private final ReceiptsArchivingExportFileRequestDTOMapper receiptsArchivingExportFileRequestDTOMapper;

  public ExportFileRetrieverServiceImpl(
    ExportFileService exportFileService,
    ExportFileMapper exportFileMapper,
    PaidExportFileRequestDTOMapper paidExportFileRequestDTOMapper,
    ReceiptsArchivingExportFileRequestDTOMapper receiptsArchivingExportFileRequestDTOMapper) {
    this.exportFileService = exportFileService;
    this.exportFileMapper = exportFileMapper;
    this.paidExportFileRequestDTOMapper = paidExportFileRequestDTOMapper;
    this.receiptsArchivingExportFileRequestDTOMapper = receiptsArchivingExportFileRequestDTOMapper;
  }

  @Override
  public PagedExportFile getExportFiles(ExportFileFiltersDTO exportFileFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    String operatorExternalUserId = null;
    if (!AuthorizationService.isAdminRole(
      exportFileFiltersDTO.getOrganizationId(), loggedUser)) {
      operatorExternalUserId = loggedUser.getMappedExternalUserId();
    }

    validateExportFileFilters(exportFileFiltersDTO);

    return exportFileMapper.mapToPagedExportFile(
      exportFileService.getExportFiles(exportFileFiltersDTO, operatorExternalUserId, pageable, accessToken), loggedUser, accessToken);
  }

  private void validateExportFileFilters(ExportFileFiltersDTO filtersDTO) {
    boolean isCreationDateEmpty = filtersDTO.getCreationDate() == null ||
      (filtersDTO.getCreationDate().getFrom() == null && filtersDTO.getCreationDate().getTo() == null);

    if (isCreationDateEmpty &&
      filtersDTO.getStatus() == null &&
      StringUtils.isBlank(filtersDTO.getFileName())) {
      throw new IllegalArgumentException("At least one of the research fields should be inserted");
    }
  }

  @Override
  public void createPaidExportFile(PaidExportFileRequest requestDTO,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(requestDTO.getOrganizationId(), loggedUser);
    exportFileService.createPaidExportFile(
      paidExportFileRequestDTOMapper.map2ProcessExecutionsDto(requestDTO),
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
    ReceiptsArchivingExportFileRequest requestDTO,
    UserInfo loggedUser, String accessToken) {
    AuthorizationService.validateUserForOrganizationId(requestDTO.getOrganizationId(), loggedUser);
    exportFileService.createReceiptsArchivingExportFile(
      receiptsArchivingExportFileRequestDTOMapper.map2ProcessExecutionsDto(requestDTO),
      accessToken);
  }
}
