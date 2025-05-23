package it.gov.pagopa.pu.bff.service.ingestion_flow_file;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.process_executions.IngestionFlowFileService;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.mapper.IngestionFlowFileMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngestionFlowFileRetrieverServiceImpl implements IngestionFlowFileRetrieverService {
  private final IngestionFlowFileService ingestionFlowFileService;
  private final IngestionFlowFileMapper ingestionFlowFileMapper;

  public IngestionFlowFileRetrieverServiceImpl(
    IngestionFlowFileService ingestionFlowFileService,
    IngestionFlowFileMapper ingestionFlowFileMapper) {
    this.ingestionFlowFileService = ingestionFlowFileService;
    this.ingestionFlowFileMapper = ingestionFlowFileMapper;
  }

  @Override
  public PagedIngestionFlowFile getIngestionFlowFiles(IngestionFlowFileFiltersDTO ingestionFlowFileFiltersDTO, Pageable pageable, UserInfo loggedUser, String accessToken) {
    String operatorExternalUserId = null;
    if (!AuthorizationService.isAdminRole(
      ingestionFlowFileFiltersDTO.getOrganizationId(), loggedUser)) {
      operatorExternalUserId = loggedUser.getMappedExternalUserId();
    }

    validateIngestionFlowFileFilters(ingestionFlowFileFiltersDTO);

    return ingestionFlowFileMapper.mapToPagedIngestionFlowFile(
      ingestionFlowFileService.getIngestionFlowFiles(
        ingestionFlowFileFiltersDTO, operatorExternalUserId, pageable, accessToken),
      loggedUser, accessToken);
  }

  private void validateIngestionFlowFileFilters(IngestionFlowFileFiltersDTO filtersDTO) {
    if (DateUtils.isNullOrInvalidDateRange(filtersDTO.getCreationDateFrom(), filtersDTO.getCreationDateTo()) &&
      filtersDTO.getStatus() == null &&
      StringUtils.isBlank(filtersDTO.getFileName())) {
      throw new IllegalArgumentException("At least one of the research fields must be provided, and both 'from' and 'to' dates must be set together");
    }
  }

}
