package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import it.gov.pagopa.pu.bff.dto.generated.ExportFile;
import it.gov.pagopa.pu.bff.dto.generated.ExportFile.StatusEnum;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.LocalDateIntervalFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import java.time.LocalDate;
import java.util.Collections;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ExportFileMapper {
  private final AuthzClient authzClient;

  public ExportFileMapper(AuthzClient authzClient) {
    this.authzClient = authzClient;
  }

  public PagedExportFile mapToPagedExportFile(
    PagedModelExportFile pagedModelExportFile, UserInfo userInfo, String accessToken) {
    PagedExportFile mappedExportFile = new PagedExportFile();
    if(pagedModelExportFile != null){
      if( pagedModelExportFile.getEmbedded() != null
        && !CollectionUtils.isEmpty(pagedModelExportFile.getEmbedded().getExportFiles())){
        mappedExportFile.setContent(pagedModelExportFile.getEmbedded().getExportFiles().stream().map(i->this.mapToExportFile(i, userInfo, accessToken)).toList());
      }else{
        mappedExportFile.setContent(Collections.emptyList());
      }
      if(pagedModelExportFile.getPage()!=null){
        mappedExportFile.setTotalPages(pagedModelExportFile.getPage().getTotalPages());
        mappedExportFile.setSize(pagedModelExportFile.getPage().getSize());
        mappedExportFile.setNumber(pagedModelExportFile.getPage().getNumber());
        mappedExportFile.setTotalElements(pagedModelExportFile.getPage().getTotalElements());
      }
    }
    return mappedExportFile;
  }

  private ExportFile mapToExportFile(
    it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile exportFile, UserInfo userInfo, String accessToken) {
    return ExportFile.builder()
      .exportFileId(exportFile.getExportFileId())
      .fileName(exportFile.getFileName())
      .creationDate(exportFile.getCreationDate())
      .totalRows(exportFile.getNumTotalRows())
      .operator(getOperator(exportFile, userInfo, accessToken))
      .status(StatusEnum.valueOf(exportFile.getStatus().toString()))
      .build();
  }

  private String getOperator(
    it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile exportFile, UserInfo userInfo, String accessToken) {
    if(!exportFile.getOperatorExternalId().equals(userInfo.getMappedExternalUserId())){
      UserInfo userInfoFromMappedExternaUserId = authzClient.getUserInfoFromMappedExternaUserId(
        exportFile.getOperatorExternalId(), accessToken);
      return userInfoFromMappedExternaUserId!=null?getOperatorString(userInfoFromMappedExternaUserId):exportFile.getOperatorExternalId();
    }else{
      return getOperatorString(userInfo);
    }
  }

  private static String getOperatorString(UserInfo userInfo) {
    return String.format("%s %s",userInfo.getFamilyName(), userInfo.getName());
  }
}
