package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile;
import it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile.StatusEnum;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class IngestionFlowFileMapper {
  private final AuthzClient authzClient;

  public IngestionFlowFileMapper(AuthzClient authzClient) {
    this.authzClient = authzClient;
  }

  public PagedIngestionFlowFile mapToPagedIngestionFlowFile(
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile, UserInfo userInfo, String accessToken) {
    PagedIngestionFlowFile mappedIngestionFlowFile = new PagedIngestionFlowFile();
    if(pagedModelIngestionFlowFile != null){
      if( pagedModelIngestionFlowFile.getEmbedded() != null
        && !CollectionUtils.isEmpty(pagedModelIngestionFlowFile.getEmbedded().getIngestionFlowFiles())){
        mappedIngestionFlowFile.setContent(pagedModelIngestionFlowFile.getEmbedded().getIngestionFlowFiles().stream().map(i->this.mapToIngestionFlowFile(i, userInfo, accessToken)).toList());
      }else{
        mappedIngestionFlowFile.setContent(Collections.emptyList());
      }
      if(pagedModelIngestionFlowFile.getPage()!=null){
        mappedIngestionFlowFile.setTotalPages(pagedModelIngestionFlowFile.getPage().getTotalPages());
        mappedIngestionFlowFile.setSize(pagedModelIngestionFlowFile.getPage().getSize());
        mappedIngestionFlowFile.setNumber(pagedModelIngestionFlowFile.getPage().getNumber());
        mappedIngestionFlowFile.setTotalElements(pagedModelIngestionFlowFile.getPage().getTotalElements());
      }
    }
    return mappedIngestionFlowFile;
  }

  private IngestionFlowFile mapToIngestionFlowFile(
    it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile ingestionFlowFile, UserInfo userInfo, String accessToken) {
    return IngestionFlowFile.builder()
      .ingestionFlowFileId(ingestionFlowFile.getIngestionFlowFileId())
      .fileName(ingestionFlowFile.getFileName())
      .creationDate(ingestionFlowFile.getCreationDate())
      .operator(getOperator(ingestionFlowFile, userInfo, accessToken))
      .totalRows(ingestionFlowFile.getNumTotalRows())
      .correctlyImportedRows(ingestionFlowFile.getNumCorrectlyImportedRows())
      .discardedRows(getDiscardedRows(ingestionFlowFile))
      .status(StatusEnum.valueOf(ingestionFlowFile.getStatus().toString()))
      .build();
  }

  private static long getDiscardedRows(
    it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile ingestionFlowFile) {
    Long totalRows = ingestionFlowFile.getNumTotalRows()!=null?ingestionFlowFile.getNumTotalRows():0L;
    Long correctlyImportedRows = ingestionFlowFile.getNumCorrectlyImportedRows()!=null?ingestionFlowFile.getNumCorrectlyImportedRows():0L;
    return Math.max(totalRows
      - correctlyImportedRows,0);
  }

  private String getOperator(
    it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile ingestionFlowFile, UserInfo userInfo, String accessToken) {
    if(StringUtils.isBlank(ingestionFlowFile.getOperatorExternalId())){
      return null;
    }

    if(!ingestionFlowFile.getOperatorExternalId().equals(userInfo.getMappedExternalUserId())){
      return getOperatorString(authzClient.getUserInfoFromMappedExternaUserId(
        ingestionFlowFile.getOperatorExternalId(), accessToken));
    }else{
      return getOperatorString(userInfo);
    }
  }

  private static String getOperatorString(UserInfo userInfo) {
    return String.format("%s %s",userInfo.getFamilyName(), userInfo.getName());
  }
}
