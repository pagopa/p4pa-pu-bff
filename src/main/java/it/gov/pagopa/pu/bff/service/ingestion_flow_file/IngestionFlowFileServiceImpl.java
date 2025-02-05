package it.gov.pagopa.pu.bff.service.ingestion_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.client.IngestionFlowFileSearchClient;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.mapper.IngestionFlowFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngestionFlowFileServiceImpl implements IngestionFlowFileService{
  private final IngestionFlowFileSearchClient ingestionFlowFileSearchClient;
  private final IngestionFlowFileMapper ingestionFlowFileMapper;

  public IngestionFlowFileServiceImpl(
    IngestionFlowFileSearchClient ingestionFlowFileSearchClient,
    IngestionFlowFileMapper ingestionFlowFileMapper) {
    this.ingestionFlowFileSearchClient = ingestionFlowFileSearchClient;
    this.ingestionFlowFileMapper = ingestionFlowFileMapper;
  }

  @Override
  public PagedIngestionFlowFile getIngestionFlowFiles(
    IngestionFlowFileFiltersDTO ingestionFlowFileFiltersDTO, Pageable pageable,
    UserInfo loggedUser, String accessToken) {
    String operatorExternalUserId = null;
    if(!AuthorizationService.isAdminRole(
        ingestionFlowFileFiltersDTO.getOrganizationId(), loggedUser)){
      operatorExternalUserId = loggedUser.getMappedExternalUserId();
    }

    return ingestionFlowFileMapper.mapToPagedIngestionFlowFile(
      ingestionFlowFileSearchClient.getIngestionFlowFiles(
          ingestionFlowFileFiltersDTO, operatorExternalUserId, pageable, accessToken),
      loggedUser,accessToken);
  }
}
