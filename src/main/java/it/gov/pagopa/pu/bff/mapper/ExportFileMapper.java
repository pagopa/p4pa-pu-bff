package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import it.gov.pagopa.pu.bff.dto.generated.ExportFile;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.util.UserUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;

@Component
public class ExportFileMapper {

  private final AuthzClient authzClient;

  public ExportFileMapper(AuthzClient authzClient) {
    this.authzClient = authzClient;
  }

  public PagedExportFile mapToPagedExportFile(
    PagedModelExportFile pagedModelExportFile, UserInfo userInfo,
    String accessToken) {
    PagedExportFile mappedExportFile = new PagedExportFile();
    if (pagedModelExportFile != null) {
      if (pagedModelExportFile.getEmbedded() != null
        && !CollectionUtils.isEmpty(
        pagedModelExportFile.getEmbedded().getExportFiles())) {
        mappedExportFile.setContent(
          pagedModelExportFile.getEmbedded().getExportFiles().stream()
            .map(i -> this.mapToExportFile(i, userInfo, accessToken)).toList());
      } else {
        mappedExportFile.setContent(Collections.emptyList());
      }
      if (pagedModelExportFile.getPage() != null) {
        mappedExportFile.setTotalPages(
          pagedModelExportFile.getPage().getTotalPages());
        mappedExportFile.setSize(pagedModelExportFile.getPage().getSize());
        mappedExportFile.setNumber(pagedModelExportFile.getPage().getNumber());
        mappedExportFile.setTotalElements(
          pagedModelExportFile.getPage().getTotalElements());
      }
    }
    return mappedExportFile;
  }

  private ExportFile mapToExportFile(
    it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile exportFile,
    UserInfo userInfo, String accessToken) {
    return ExportFile.builder()
      .exportFileId(exportFile.getExportFileId())
      .fileName(exportFile.getFileName())
      .fileSize(exportFile.getFileSize())
      .creationDate(exportFile.getCreationDate())
      .totalRows(exportFile.getNumTotalRows())
      .operator(UserUtils.getOperator(exportFile.getOperatorExternalId(), userInfo,
        authzClient.getUserInfoFromMappedExternalUserId(
          exportFile.getOperatorExternalId(), accessToken)))
      .status(exportFile.getStatus())
      .build();
  }

}
