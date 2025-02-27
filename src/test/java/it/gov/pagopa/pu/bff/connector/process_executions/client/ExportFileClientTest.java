package it.gov.pagopa.pu.bff.connector.process_executions.client;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.config.ProcessExecutionsApisHolder;
import it.gov.pagopa.pu.processexecutions.controller.generated.ExportFileControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ExportFileClientTest {

  @Mock
  private ProcessExecutionsApisHolder processExecutionsApisHolderMock;
  @Mock
  private ExportFileControllerApi exportFileControllerApiMock;

  private ExportFileClient exportFileClient;


  @Test
  void whenCreateExportFileThenOk() {
    ExportFileRequestDTO requestDTO = ExportFileRequestDTO.builder()
      .organizationId(1L)
      .flowFileType(ExportFileRequestDTO.FlowFileTypeEnum.CLASSIFICATIONS)
      .filterFields(ExportFileFilter.builder()
        .iuv("iuv")
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    when(processExecutionsApisHolderMock.getExportFileControllerApi(accessToken))
      .thenReturn(exportFileControllerApiMock);

    exportFileClient.createExportFile(requestDTO, accessToken);

    verify(exportFileControllerApiMock).createExportFile(requestDTO);
  }


}
