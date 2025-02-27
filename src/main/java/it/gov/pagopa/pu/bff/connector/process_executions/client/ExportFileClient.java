package it.gov.pagopa.pu.bff.connector.process_executions.client;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExportFileClient {

  private final ProcessExecutionsApisHolder processExecutionsApisHolder;

  public ExportFileClient(
    ProcessExecutionsApisHolder processExecutionsApisHolder) {
    this.processExecutionsApisHolder = processExecutionsApisHolder;
  }


  public void createExportFile(ExportFileRequestDTO requestDTO, String accessToken) {
    processExecutionsApisHolder.getExportFileControllerApi(accessToken)
      .createExportFile(requestDTO);
  }

}
